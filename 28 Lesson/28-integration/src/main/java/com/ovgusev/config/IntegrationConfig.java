package com.ovgusev.config;

import com.ovgusev.domain.TickerInfo;
import com.ovgusev.entity.AgregatedTickerInfo;
import com.ovgusev.service.TickerInfoAggregationService;
import com.ovgusev.service.TickerInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.aggregator.TimeoutCountSequenceSizeReleaseStrategy;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.store.SimpleMessageStore;

import javax.persistence.EntityManagerFactory;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class IntegrationConfig {
    private static final Duration POLLING_DURATION = Duration.ofMillis(10L);

    @Bean
    public IntegrationFlow integrationFlow(EntityManagerFactory entityManagerFactory, TickerInfoService tickerInfoService) {
        return IntegrationFlows
                // часто опрашиваем источник - сервис
                .from(tickerInfoService::getTickerList,
                        e -> e.poller(Pollers.fixedDelay(POLLING_DURATION)))
                // сплитим на элементы массива
                .split()
                // по каждому элементу запрашиваем текущее состояние
                .transform(tickerInfoService.getCurrentTickerInfo())
                // собираем в одну пачку информацию по разным тикерам
                .aggregate()
                // отправляем в очередь для накопления и последующей агрегации
                .aggregate(aggregatorSpec -> aggregatorSpec
                        .correlationStrategy(message -> 1)
                        .expireGroupsUponCompletion(true)
                        .releaseStrategy(new TimeoutCountSequenceSizeReleaseStrategy(TimeoutCountSequenceSizeReleaseStrategy.DEFAULT_THRESHOLD, TickerInfoAggregationService.AGGREGATE_DURATION.toMillis()))
                        .messageStore(new SimpleMessageStore(5000))
                )
                // разворачиваем листы в отдельные сообщения
                .transform(source -> ((List<List<TickerInfo>>) source).stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()))
                // агрегируем множество записей. Тикер - группируем, цену - усредняем, дата - максимальная
                .transform(source -> TickerInfoAggregationService.aggregateTickerInfo((List<TickerInfo>) source))
                // Персистим
                .handle(Jpa.outboundAdapter(entityManagerFactory)
                                .entityClass(AgregatedTickerInfo.class)
                        , ConsumerEndpointSpec::transactional
                )
                .get();
    }
}
