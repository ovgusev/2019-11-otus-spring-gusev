package com.ovgusev.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TickerInfo {
    /**
     * Краткий биржевой идентификатор ("тикер").
     */
    private String ticker;

    /**
     * Момент времени, на который актуальна запись
     */
    private LocalDateTime dateTime;

    /**
     * Цена последней сделки
     */
    private BigDecimal lastPrice;
}
