package com.example.rsocketspring.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class BatchJobController {
	@MessageMapping("batch.job.request")
	public Mono<Void> submitJob(Mono<Integer> integerMono, RSocketRequester requester) {
		System.out.println("server commit the job");
		process(integerMono, requester);
		return Mono.empty();
	}

	private void process(Mono<Integer> integerMono, RSocketRequester requester) {
		integerMono.map(i -> i * i * i)
		           .flatMap(qube -> requester.route("batch.job.response").data(qube).send()) // <- client implement
		           .subscribe();
	}
}
