package com.example.userservice.controller;

import com.example.userservice.dto.TransactionRequest;
import com.example.userservice.dto.TransactionResponse;
import com.example.userservice.service.UserTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@MessageMapping("user")
@RequiredArgsConstructor
public class UserTransactionController {
	private final UserTransactionService userTransactionService;

	@MessageMapping("transaction")
	public Mono<TransactionResponse> doTransaction(Mono<TransactionRequest> requestMono) {
		return requestMono.flatMap(userTransactionService::doTransaction);
	}
}
