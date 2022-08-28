package com.example.stockservice.domain;

import lombok.Getter;

import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Stock {
	private int price;
	private final String code;
	private final int volatility;

	public Stock(int price, String code, int volatility) {
		this.price = price;
		this.code = code;
		this.volatility = volatility;
	}

	public int getPrice() {
		this.updatePrice();
		return price;
	}

	private void updatePrice() {
		int rand = ThreadLocalRandom.current().nextInt(-1 * volatility, volatility + 1);
		this.price += rand;
		this.price = Math.max(this.price, 0);
	}
}
