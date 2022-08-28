package com.example.tradingservice.dto.stock;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTickDto {
	private String code;
	private int price;

}
