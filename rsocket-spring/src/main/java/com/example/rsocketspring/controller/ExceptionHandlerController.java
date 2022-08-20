package com.example.rsocketspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("math.exception.handler")
public class ExceptionHandlerController {
	@MessageMapping("double.{input}")
	public Mono<Integer> doubleItSwitchIfEmpty(@DestinationVariable int input) {
		return Mono.just(input)
		           .filter(integer -> integer < 31)
		           .map(integer -> integer * 2)
		           .switchIfEmpty(Mono.error(new IllegalArgumentException("can not be > 30")));
	}

	// like ControllerAdvice, but doesn't locate it ControllerAdvice;
	// Only located in Controller class (works only on that controller you specified)
	// @MessageExceptionHandler // <- Any Exception
	@MessageExceptionHandler(IllegalArgumentException.class)
	public Mono<Integer> handleException(Exception exception) {
		return Mono.just(-1);
	}
}
