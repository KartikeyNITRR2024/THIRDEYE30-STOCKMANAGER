package com.thirdeye3.stockmanager.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StockDto {
    private Long uniqueId;
    private String uniqueCode;
    private String marketCode;
    private Double lastNightClosingPrice;
    private Double todaysOpeningPrice;
	private Timestamp currentTime;
}
