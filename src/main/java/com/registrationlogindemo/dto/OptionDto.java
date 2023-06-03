package com.registrationlogindemo.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OptionDto {
    private Long id;
    @Digits(integer = 11, fraction = 2, message = "Please enter a valid Strike Price (2 decimal places)")
    @DecimalMin("0.00")
    @DecimalMax("99999999999.00")
    @NotNull(message = "Strike Price must not be empty")
    private BigDecimal strikePrice;
    private BigDecimal price;
    @NotNull
    private LocalDate expiryDate;
    @NotNull
    private LocalDate purchaseDate;
    @NotNull
    private boolean optionType; //Whether the option is a call or a put option, true = call option
    @Positive(message = "Trader IDs are strictly positive and must not be empty")
    @NotNull
    private int traderId;

}
