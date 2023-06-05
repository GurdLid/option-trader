package com.registrationlogindemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal strikePrice;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private LocalDate expiryDate;
    @Column(nullable = false)
    private LocalDate purchaseDate;
    @Column(nullable = false)
    private boolean optionType;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    private User owner;

    @Column
    private BigDecimal profit;

    @Column(nullable = false,columnDefinition = "boolean default false")
    private Boolean resolved = false;

    @Override
    public String toString() {
        return "Option{" +
                "id=" + id +
                ", strikePrice=" + strikePrice +
                ", price=" + price +
                ", expiryDate=" + expiryDate +
                ", purchaseDate=" + purchaseDate +
                ", optionType=" + optionType +
                ", profit=" + profit +
                ", resolved=" + resolved +
                '}';
    }
}