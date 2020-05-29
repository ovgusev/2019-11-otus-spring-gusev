package com.ovgusev.service;

import com.ovgusev.domain.TickerInfo;
import com.ovgusev.entity.AgregatedTickerInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TickerInfoAggregationService {
    public static final Duration AGGREGATE_DURATION = Duration.ofMillis(1000L);

    public List<AgregatedTickerInfo> aggregateTickerInfo(List<TickerInfo> tickerInfoList) {
        List<AgregatedTickerInfo> result = new ArrayList<>();

        Map<String, List<TickerInfo>> mapByTicker = tickerInfoList.stream()
                .collect(Collectors.groupingBy(TickerInfo::getTicker));

        mapByTicker.forEach((k, v) -> {
            result.add(new AgregatedTickerInfo()
                    .setTicker(k)
                    .setPeriod(AGGREGATE_DURATION.toSeconds())
                    .setPrice(getAvgBigDecimal(v))
                    .setDateTime(getMaxDateTime(v))
            );
        });

        return result;
    }

    private static BigDecimal getAvgBigDecimal(List<TickerInfo> tickerInfoList) {
        if (tickerInfoList == null || tickerInfoList.isEmpty()) {
            return null;
        } else {
            return tickerInfoList.stream()
                    .map(TickerInfo::getLastPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(tickerInfoList.size()), RoundingMode.HALF_UP);
        }
    }

    private static LocalDateTime getMaxDateTime(List<TickerInfo> tickerInfoList) {
        if (tickerInfoList == null || tickerInfoList.isEmpty()) {
            return null;
        } else {
            return tickerInfoList.stream()
                    .map(TickerInfo::getDateTime)
                    .reduce((dateTime, dateTime2) -> dateTime.compareTo(dateTime2) > 0 ? dateTime : dateTime2)
                    .orElse(null);
        }
    }
}
