package com.optiontrader.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "options") //This is the name of the table in the MySQL DB
public class Option {
    /**
     * Using Lombok along with JPA for reducing boilerplate code and easy interactions with the MySQL database.
     * An option has:
     * an id value
     * a strikePrice(how much the stock can be purchased for at expiry)
     * an expiryDate (when the option expires)
     * a purchaseDate (when the option was purchased)
     * an optionType (Call option or put option)
     * a price (a calculated value from the above which is how much the option was bought for)
     * an owner
     * the profit recieved from the option when it expires
     * a boolean to demonstrate that the option has been resolved since it has expired.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Auto generated ID
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

    @ManyToOne(fetch=FetchType.LAZY) //An option can have only one owner, but a user can have many options
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