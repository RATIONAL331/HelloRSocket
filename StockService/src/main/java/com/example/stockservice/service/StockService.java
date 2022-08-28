package com.example.stockservice.service;

import com.example.stockservice.domain.Stock;
import com.example.stockservice.dto.StockTickDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class StockService {
	private static final Stock AMZN = new Stock(1000, "AMZN", 20);
	private static final Stock AAPL = new Stock(100, "AAPL", 3);
	private static final Stock MSFT = new Stock(200, "MSFT", 5);

	public Flux<StockTickDto> getStockPrice() {
		return Flux.interval(Duration.ofSeconds(1))
		           .flatMap(i -> Flux.just(AMZN, AAPL, MSFT))
		           .map(s -> new StockTickDto(s.getCode(), s.getPrice()));
	}
}
