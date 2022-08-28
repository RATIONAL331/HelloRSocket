package com.example.tradingservice.repository;

import com.example.tradingservice.entity.UserStock;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserStockRepository extends ReactiveMongoRepository<UserStock, String> {
	Mono<UserStock> findByUserIdAndStockSymbol(String userId, String stockSymbol);

	Flux<UserStock> findAllByUserId(String userId);
}
