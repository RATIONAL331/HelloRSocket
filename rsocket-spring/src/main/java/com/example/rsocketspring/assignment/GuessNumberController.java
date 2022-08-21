package com.example.rsocketspring.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class GuessNumberController {
	private final GuessNumberService guessNumberService;

	@MessageMapping("guess.a.number")
	public Flux<GuessNumberResponse> play(Flux<Integer> flux) {
		return guessNumberService.play(flux);
	}
}
