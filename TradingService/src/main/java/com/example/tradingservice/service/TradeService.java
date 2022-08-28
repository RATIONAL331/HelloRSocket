package com.example.tradingservice.service;

import com.example.tradingservice.client.StockClient;
import com.example.tradingservice.client.UserClient;
import com.example.tradingservice.dto.StockTradeRequest;
import com.example.tradingservice.dto.StockTradeResponse;
import com.example.tradingservice.dto.TradeStatus;
import com.example.tradingservice.dto.TradeType;
import com.example.tradingservice.dto.user.TransactionRequest;
import com.example.tradingservice.dto.user.TransactionStatus;
import com.example.tradingservice.utils.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {
	private final UserStockService userStockService;
	private final UserClient userClient;
	private final StockClient stockClient;

	public Mono<StockTradeResponse> trade(StockTradeRequest request) {
		TransactionRequest transactionRequest = EntityDtoUtil.toTransactionRequest(request, this.estimatePrice(request));
		Mono<StockTradeResponse> stockTradeResponseMono = TradeType.BUY.equals(request.getTradeType()) ? buyStock(request, transactionRequest) : sellStock(request, transactionRequest);
		return stockTradeResponseMono.defaultIfEmpty(EntityDtoUtil.toTradeResponse(request, TradeStatus.FAILED, 0));
	}

	private Mono<StockTradeResponse> buyStock(StockTradeRequest stockTradeRequest, TransactionRequest transactionRequest) {
		return userClient.doTransaction(transactionRequest)
		                 .filter(tr -> TransactionStatus.COMPLETED.equals(tr.getStatus()))
		                 .flatMap(tr -> userStockService.buyStock(stockTradeRequest))
		                 .map(us -> EntityDtoUtil.toTradeResponse(stockTradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount()));
	}

	private Mono<StockTradeResponse> sellStock(StockTradeRequest stockTradeRequest, TransactionRequest transactionRequest) {
		return userStockService.sellStock(stockTradeRequest)
		                       .flatMap(us -> userClient.doTransaction(transactionRequest))
		                       .map(us -> EntityDtoUtil.toTradeResponse(stockTradeRequest, TradeStatus.COMPLETED, transactionRequest.getAmount()));
	}


	private int estimatePrice(StockTradeRequest request) {
		return request.getQuantity() * stockClient.getCurrentStockPrice(request.getStockSymbol());
	}
}
