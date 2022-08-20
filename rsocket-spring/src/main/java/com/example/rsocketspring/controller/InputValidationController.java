package com.example.rsocketspring.controller;

import com.example.rsocketspring.dto.Response;
import com.example.rsocketspring.dto.error.ErrorEvent;
import com.example.rsocketspring.dto.error.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("math.validation")
public class InputValidationController {
	@MessageMapping("double.{input}")
	public Mono<Integer> doubleIt(@DestinationVariable int input) {
		if (input < 31) {
			return Mono.just(input * 2);
		}
		return Mono.error(new IllegalArgumentException("can not be > 30"));
	}

	@MessageMapping("double.{input}.defaultIfEmpty")
	public Mono<Integer> doubleItDefaultIfEmpty(@DestinationVariable int input) {
		return Mono.just(input)
		           .filter(integer -> integer < 31)
		           .map(integer -> integer * 2)
		           .defaultIfEmpty(Integer.MIN_VALUE);
	}

	@MessageMapping("double.{input}.switchIfEmpty")
	public Mono<Integer> doubleItSwitchIfEmpty(@DestinationVariable int input) {
		return Mono.just(input)
		           .filter(integer -> integer < 31)
		           .map(integer -> integer * 2)
		           .switchIfEmpty(Mono.error(new IllegalArgumentException("can not be > 30")));
	}

	@MessageMapping("double.response.{input}")
	public Mono<Response<Integer>> doubleResponse(@DestinationVariable int input) {
		return Mono.just(input)
		           .filter(integer -> integer < 31)
		           .map(integer -> integer * 2)
		           .map(Response::with)
		           .defaultIfEmpty(Response.with(new ErrorEvent(StatusCode.EC001)));
	}
}
