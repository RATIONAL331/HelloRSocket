package com.rational331.rsocket.service;

import com.rational331.rsocket.dto.ChartResponseDto;
import com.rational331.rsocket.dto.RequestDto;
import com.rational331.rsocket.dto.ResponseDto;
import com.rational331.rsocket.utils.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class MathService implements RSocket {
	// Sign out; Delete User; Logging; Raising an event
	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		System.out.println("Received: " + ObjectUtil.toObject(payload, RequestDto.class));
		return Mono.empty();
	}

	// Any typical request response. create user...
	@Override
	public Mono<Payload> requestResponse(Payload payload) {
		return Mono.fromSupplier(() -> {
			RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
			ResponseDto responseDto = new ResponseDto(requestDto.getInput(), requestDto.getInput() * requestDto.getInput());
			return ObjectUtil.toPayload(responseDto);
		});
	}

	// Search results; Order updates; Uber Driver location updates; Periodic data emission
	@Override
	public Flux<Payload> requestStream(Payload payload) {
		RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
		return Flux.range(1, 10)
		           .map(i -> i * requestDto.getInput())
		           .map(res -> new ResponseDto(requestDto.getInput(), res))
		           .delayElements(Duration.ofMillis(500))
		           .doOnNext(System.out::println)
		           .doFinally(s -> System.out.println("Stream completed: " + s))
		           .map(ObjectUtil::toPayload);
	}

	// Interactive communication like Game, Chat...
	@Override
	public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
		return Flux.from(payloads)
		           .map(payload -> ObjectUtil.toObject(payload, RequestDto.class))
		           .map(RequestDto::getInput)
		           .map(i -> new ChartResponseDto(i, i * i + 1))
		           .map(ObjectUtil::toPayload);
	}
}
