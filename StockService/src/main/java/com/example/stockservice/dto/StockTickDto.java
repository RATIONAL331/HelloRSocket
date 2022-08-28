package com.example.stockservice.dto;

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
