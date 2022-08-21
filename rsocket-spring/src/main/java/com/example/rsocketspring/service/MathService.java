package com.example.rsocketspring.service;

import com.example.rsocketspring.dto.ChartResponseDto;
import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class MathService {
	// fire-forget
	public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono) {
		return requestDtoMono.doOnNext(System.out::println)
		                     .then();
	}

	// request-response
	public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
		return requestDtoMono.map(ComputationRequestDto::getInput)
		                     .map(res -> new ComputationResponseDto(res, res * res));
	}

	// request-stream
	public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono) {
		return requestDtoMono.flatMapMany(requestDto -> Flux.range(1, 1000)
		                                                    .delayElements(Duration.ofMillis(10))
		                                                    .map(integer -> new ComputationResponseDto(requestDto.getInput(),
		                                                                                               requestDto.getInput() * integer)));
	}

	// request-channel
	public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoFlux) {
		return requestDtoFlux.map(ComputationRequestDto::getInput)
		                     .map(res -> new ChartResponseDto(res, (res * res) + 1));
	}
}
