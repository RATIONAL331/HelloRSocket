package com.example.tradingservice.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
public class UserStock {
	@Id
	private String id;
	private String userId;
	private String stockSymbol;
	private Integer quantity;
}
