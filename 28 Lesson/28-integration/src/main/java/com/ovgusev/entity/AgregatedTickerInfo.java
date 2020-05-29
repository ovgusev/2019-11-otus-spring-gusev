package com.ovgusev.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticker_info")
@Data
@Accessors(chain = true)
public class AgregatedTickerInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "period")
    private Long period;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "price")
    private BigDecimal price;
}
