package com.example.stockservice.controller;

import com.example.stockservice.dto.StockTickDto;
import com.example.stockservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class StockController {
	private final StockService service;

	@MessageMapping("stock.ticks")
	public Flux<StockTickDto> getStockPrice() {
		return service.getStockPrice();
	}
}
