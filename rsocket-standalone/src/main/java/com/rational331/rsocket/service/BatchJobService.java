package com.rational331.rsocket.service;

import com.rational331.rsocket.dto.RequestDto;
import com.rational331.rsocket.dto.ResponseDto;
import com.rational331.rsocket.utils.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
public class BatchJobService implements RSocket {
	private final RSocket rSocket;

	@Override
	public Mono<Void> fireAndForget(Payload payload) {
		RequestDto requestDto = ObjectUtil.toObject(payload, RequestDto.class);
		System.out.println("Received: " + requestDto);

		Mono.just(requestDto)
		    .delayElement(Duration.ofSeconds(1))
		    .doOnNext(i -> System.out.println("Emitting: " + i))
		    .flatMap(this::findCube)
		    .subscribe();

		return Mono.empty();
	}

	private Mono<Void> findCube(RequestDto requestDto) {
		int input = requestDto.getInput();
		int output = input * input * input;
		ResponseDto responseDto = new ResponseDto(input, output);
		Payload payload = ObjectUtil.toPayload(responseDto);
		return rSocket.fireAndForget(payload);
	}
}
