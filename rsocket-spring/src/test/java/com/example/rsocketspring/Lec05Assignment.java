package com.example.rsocketspring;


import com.example.rsocketspring.assignment.GuessNumberResponse;
import com.example.rsocketspring.assignment.Player;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05Assignment {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		requester = builder.transport(TcpClientTransport.create("localhost", 6565));
	}

	@Test
	public void assignment() {
		Player player = new Player();
		requester.route("guess.a.number")
		         .data(player.guesses().delayElements(Duration.ofSeconds(1)))
		         .retrieveFlux(GuessNumberResponse.class)
		         .doOnNext(player.receives())
		         .doFirst(player::play)
		         .then() // just return Mono<Void>
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

}
