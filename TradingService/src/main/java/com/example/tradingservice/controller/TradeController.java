package com.example.tradingservice.controller;

import com.example.tradingservice.client.StockClient;
import com.example.tradingservice.dto.StockTradeRequest;
import com.example.tradingservice.dto.StockTradeResponse;
import com.example.tradingservice.dto.stock.StockTickDto;
import com.example.tradingservice.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("stock")
@RequiredArgsConstructor
public class TradeController {
	private final StockClient stockClient;
	private final TradeService tradeService;

	@GetMapping(value = "tick/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<StockTickDto> stockTicks() {
		return stockClient.getStockStream();
	}

	@PostMapping("trade")
	public Mono<ResponseEntity<StockTradeResponse>> trade(@RequestBody Mono<StockTradeRequest> tradeRequestMono) {
		return tradeRequestMono.filter(tr -> tr.getQuantity() > 0)
		                       .flatMap(tradeService::trade)
		                       .map(ResponseEntity::ok)
		                       .defaultIfEmpty(ResponseEntity.badRequest().build());
	}
}
