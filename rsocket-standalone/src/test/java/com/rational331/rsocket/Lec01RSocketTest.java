package com.rational331.rsocket;

import com.rational331.rsocket.dto.ChartResponseDto;
import com.rational331.rsocket.dto.RequestDto;
import com.rational331.rsocket.dto.ResponseDto;
import com.rational331.rsocket.utils.ObjectUtil;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

// YOU MUST START SERVER BEFORE RUNNING THIS TEST
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec01RSocketTest {
	private RSocket rSocket;

	@BeforeAll
	public void setup() {
		rSocket = RSocketConnector.create()
		                          .connect(TcpClientTransport.create("localhost", 6565))
		                          .block();
	}

	@Test
	public void fireAndForget() {
		Payload payload = DefaultPayload.create("Hello World!");
		Mono<Void> voidMono = rSocket.fireAndForget(payload);

		StepVerifier.create(voidMono)
		            .verifyComplete();
	}

	@RepeatedTest(3)
	public void repeatFireAndForget() {
		Payload payload = DefaultPayload.create("Hello World!");
		Mono<Void> voidMono = rSocket.fireAndForget(payload);

		StepVerifier.create(voidMono)
		            .verifyComplete();
	}

	@Test
	public void fireAndForgetWithObjectUtil() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Mono<Void> voidMono = rSocket.fireAndForget(payload);

		StepVerifier.create(voidMono)
		            .verifyComplete();
	}

	@Test
	public void request_response() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Mono<ResponseDto> responseDtoMono = rSocket.requestResponse(payload)
		                                           .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
		                                           .doOnNext(System.out::println);

		StepVerifier.create(responseDtoMono)
		            .expectNextCount(1)
		            .verifyComplete();
	}

	@Test
	public void request_stream() {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));
		Flux<ResponseDto> responseDtoFlux = rSocket.requestStream(payload)
		                                           .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
		                                           .doOnNext(System.out::println)
		                                           .take(4);

		StepVerifier.create(responseDtoFlux)
		            .expectNextCount(4)
		            .verifyComplete();
	}

	@Test
	public void request_channel() {
		Flux<Payload> payloadFlux = Flux.range(-10, 21)
		                                .delayElements(Duration.ofMillis(500))
		                                .map(RequestDto::new)
		                                .map(ObjectUtil::toPayload);

		Flux<ChartResponseDto> responseDtoFlux = rSocket.requestChannel(payloadFlux)
		                                                .map(p -> ObjectUtil.toObject(p, ChartResponseDto.class))
		                                                .doOnNext(System.out::println);

		StepVerifier.create(responseDtoFlux)
		            .expectNextCount(21)
		            .verifyComplete();
	}
}