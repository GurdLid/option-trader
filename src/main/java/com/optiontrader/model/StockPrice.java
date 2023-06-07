package com.optiontrader.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stockprices")
public class StockPrice {
    /**
     * Stock prices have a date and the associated price at that date,
     * where the date acts as the Primary Key
     */
    @Id
    private LocalDate date;
    @Column(nullable = false)
    private BigDecimal price;


}
