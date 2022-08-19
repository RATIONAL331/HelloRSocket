package com.example.rsocketspring.controller;

import com.example.rsocketspring.dto.ChartResponseDto;
import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import com.example.rsocketspring.service.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class MathController {
	private final MathService mathService;

	@MessageMapping("math.service.print")
	public Mono<Void> print(Mono<ComputationRequestDto> requestDtoMono) {
		return mathService.print(requestDtoMono);
	}

	@MessageMapping("math.service.square")
	public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono) {
		return mathService.findSquare(requestDtoMono);
	}

	@MessageMapping("math.service.table")
	public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono) {
		return mathService.tableStream(requestDtoMono);
	}

	@MessageMapping("math.service.chart")
	public Flux<ChartResponseDto> chartStream(Flux<ComputationRequestDto> requestDtoMono) {
		return mathService.chartStream(requestDtoMono);
	}
}
