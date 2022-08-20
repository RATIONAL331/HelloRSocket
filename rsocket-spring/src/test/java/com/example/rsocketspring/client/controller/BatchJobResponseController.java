package com.example.rsocketspring.client.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobResponseController {
	@MessageMapping("batch.job.response")
	public Mono<Void> response(Mono<Integer> integerMono) {
		return integerMono.doOnNext(integer -> System.out.println("Client Received: " + integer))
		                  .then();
	}
}
