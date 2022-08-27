package com.example.rsocketspring.security;

import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import com.example.rsocketspring.service.MathService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@MessageMapping("math.service.secured")
public class SecureMathController {
	private final MathService mathService;

	@MessageMapping("square")
	@PreAuthorize("hasRole('USER')")
	public Mono<ComputationResponseDto> findSquare(Mono<ComputationRequestDto> requestDtoMono,
	                                               @AuthenticationPrincipal Mono<UserDetails> userDetailsMono) {
		userDetailsMono.doOnNext(System.out::println).subscribe();
		return mathService.findSquare(requestDtoMono);
	}

	@MessageMapping("table")
	public Flux<ComputationResponseDto> tableStream(Mono<ComputationRequestDto> requestDtoMono) {
		return mathService.tableStream(requestDtoMono);
	}
}
