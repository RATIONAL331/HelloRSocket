package com.example.userservice.service;

import com.example.userservice.dto.TransactionRequest;
import com.example.userservice.dto.TransactionResponse;
import com.example.userservice.dto.TransactionStatus;
import com.example.userservice.dto.TransactionType;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.utils.EntityDtoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserTransactionService {
	private final UserRepository userRepository;

	public Mono<TransactionResponse> doTransaction(TransactionRequest transactionRequest) {
		return userRepository.findById(transactionRequest.getUserId())
		                     .transform(TransactionType.CREDIT.equals(transactionRequest.getType()) ? credit(transactionRequest) : debit(transactionRequest))
		                     .flatMap(userRepository::save)
		                     .map(s -> EntityDtoUtil.toResponse(transactionRequest, TransactionStatus.COMPLETED))
		                     .defaultIfEmpty(EntityDtoUtil.toResponse(transactionRequest, TransactionStatus.FAILED));
	}

	private Function<Mono<User>, Mono<User>> credit(TransactionRequest request) {
		return userMono -> userMono.doOnNext(u -> u.setBalance(u.getBalance() + request.getAmount()));
	}

	private Function<Mono<User>, Mono<User>> debit(TransactionRequest request) {
		return userMono -> userMono.filter(u -> u.getBalance() >= request.getAmount())
		                           .doOnNext(u -> u.setBalance(u.getBalance() - request.getAmount()));
	}
}
