package com.example.tradingservice.utils;

import com.example.tradingservice.dto.*;
import com.example.tradingservice.dto.user.TransactionRequest;
import com.example.tradingservice.dto.user.TransactionType;
import com.example.tradingservice.entity.UserStock;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
	public static UserStock toUserStock(StockTradeRequest request) {
		UserStock userStock = new UserStock();
		userStock.setUserId(request.getUserId());
		userStock.setStockSymbol(request.getStockSymbol());
		userStock.setQuantity(0);
		return userStock;
	}

	public static TransactionRequest toTransactionRequest(StockTradeRequest request, int amount) {
		TransactionRequest transactionRequest = new TransactionRequest();
		TransactionType transactionType = TradeType.BUY.equals(request.getTradeType()) ? TransactionType.DEBIT : TransactionType.CREDIT;
		transactionRequest.setType(transactionType);
		transactionRequest.setUserId(request.getUserId());
		transactionRequest.setAmount(amount);
		return transactionRequest;
	}

	public static StockTradeResponse toTradeResponse(StockTradeRequest request, TradeStatus tradeStatus, int price) {
		StockTradeResponse stockTradeResponse = new StockTradeResponse();
		BeanUtils.copyProperties(request, stockTradeResponse);
		stockTradeResponse.setTradeStatus(tradeStatus);
		stockTradeResponse.setPrice(price);
		return stockTradeResponse;
	}

	public static UserStockDto toUserStockDto(UserStock stock) {
		UserStockDto userStockDto = new UserStockDto();
		BeanUtils.copyProperties(stock, userStockDto);
		return userStockDto;
	}
}
