package com.example.tradingservice.service;

import com.example.tradingservice.dto.StockTradeRequest;
import com.example.tradingservice.dto.UserStockDto;
import com.example.tradingservice.entity.UserStock;
import com.example.tradingservice.repository.UserStockRepository;
import com.example.tradingservice.utils.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserStockService {
	private final UserStockRepository userStockRepository;

	public Flux<UserStockDto> getUserStocks(String userId) {
		return userStockRepository.findAllByUserId(userId)
		                          .map(EntityDtoUtil::toUserStockDto);
	}

	// buy
	public Mono<UserStock> buyStock(StockTradeRequest request) {
		return userStockRepository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
		                          .defaultIfEmpty(EntityDtoUtil.toUserStock(request))
		                          .doOnNext(us -> us.setQuantity(us.getQuantity() + request.getQuantity()))
		                          .flatMap(userStockRepository::save);
	}

	// sell
	public Mono<UserStock> sellStock(StockTradeRequest request) {
		return userStockRepository.findByUserIdAndStockSymbol(request.getUserId(), request.getStockSymbol())
		                          .filter(us -> us.getQuantity() >= request.getQuantity())
		                          .doOnNext(us -> us.setQuantity(us.getQuantity() - request.getQuantity()))
		                          .flatMap(userStockRepository::save);
	}
}
