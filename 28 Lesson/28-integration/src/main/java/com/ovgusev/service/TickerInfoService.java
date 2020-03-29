package com.ovgusev.service;

import com.ovgusev.domain.TickerInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
// какой-то сервис, выдающий цены. логику наидал просто чтоб циферки были красивые
public class TickerInfoService {
    private final Random random = new Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
    private final Map<String, TickerInfo> lastTickerInfoMap = new ConcurrentHashMap<>();

    public List<String> getTickerList() {
        return List.of("AAA", "BBB", "CCC", "DDD");
    }

    public Function<String, TickerInfo> getCurrentTickerInfo() {
        return (ticker) -> {
            LocalDateTime now = LocalDateTime.now();

            TickerInfo tickerInfo = lastTickerInfoMap.computeIfAbsent(ticker,
                    s -> getInit(ticker, now)
            );

            return lastTickerInfoMap.put(ticker, getNext(tickerInfo, now));
        };
    }

    private TickerInfo getNext(TickerInfo tickerInfo, LocalDateTime dateTime) {
        BigDecimal oldPrice = tickerInfo.getLastPrice();

        BigDecimal newPrice = oldPrice
                .multiply(getRandomMultiplicator(tickerInfo.getDateTime(), dateTime))
                .setScale(2, RoundingMode.HALF_UP);

        return new TickerInfo()
                .setTicker(tickerInfo.getTicker())
                .setDateTime(dateTime)
                .setLastPrice(newPrice);
    }

    private BigDecimal getRandomMultiplicator(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        BigDecimal volatilityPerSecond = BigDecimal.valueOf(random.nextGaussian());
        Duration duration = Duration.between(startDateTime, endDateTime);
        BigDecimal durationInSeconds = BigDecimal.valueOf(duration.toMillis()).scaleByPowerOfTen(-3);
        BigDecimal volatility = volatilityPerSecond.multiply(durationInSeconds);

        return BigDecimal.valueOf(Math.pow(20, volatility.doubleValue() / 100));
    }

    private TickerInfo getInit(String ticker, LocalDateTime dateTime) {
        return new TickerInfo()
                .setTicker(ticker)
                .setDateTime(dateTime)
                .setLastPrice(BigDecimal.valueOf(random.nextFloat()).scaleByPowerOfTen(2));
    }
}
