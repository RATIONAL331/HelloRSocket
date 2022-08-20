package com.example.rsocketspring;

import com.example.rsocketspring.dto.ChartResponseDto;
import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec01RSocketTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		requester = builder.transport(TcpClientTransport.create("localhost", 6565));
	}

	@Test
	public void fireAndForget() {
		requester.route("math.service.print")
		         .data(new ComputationRequestDto(5))
		         .send()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	@Test
	public void requestResponse() {
		requester.route("math.service.square")
		         .data(new ComputationRequestDto(5))
		         .retrieveMono(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	public void requestStream() {
		requester.route("math.service.table")
		         .data(new ComputationRequestDto(5))
		         .retrieveFlux(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(10)
		         .verifyComplete();
	}

	@Test
	public void requestChannel() {
		requester.route("math.service.chart")
		         .data(Flux.range(-10, 21).map(ComputationRequestDto::new))
		         .retrieveFlux(ChartResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(21)
		         .verifyComplete();
	}
}
