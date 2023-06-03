package com.registrationlogindemo.model;

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
    @Id
    private LocalDate date;
    @Column(nullable = false)
    private BigDecimal price;

    public static List<StockPrice> allPrices = new ArrayList<>();

    public static List<LocalDate> allDates = new ArrayList<>();



}
