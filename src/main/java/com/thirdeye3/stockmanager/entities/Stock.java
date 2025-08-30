package com.thirdeye3.stockmanager.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "STOCK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STOCK_ID")
	private Long uniqueId;
    
    @Column(name = "STOCK_UNIQUE_CODE")
	private String uniqueCode;
    
    @Column(name = "STOCK_MARKET_CODE")
	private String marketCode;

    @Column(name = "LASTNIGHT_CLOSING_PRICE")
    private Double lastNightClosingPrice;

    @Column(name = "TODAYS_OPENING_PRICE")
    private Double todaysOpeningPrice;
}
