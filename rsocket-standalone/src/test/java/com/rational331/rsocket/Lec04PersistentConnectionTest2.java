package com.rational331.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketClient;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04PersistentConnectionTest2 {
	private RSocketClient rSocketClient;

	@BeforeAll
	public void setup() {
		Mono<RSocket> rSocketMono = RSocketConnector.create()
		                                            .connect(TcpClientTransport.create("localhost", 6565))
		                                            .doOnNext(r -> System.out.println("going to connect"));

		this.rSocketClient = RSocketClient.from(rSocketMono);
	}

	@Test
	public void connectionTest() throws Exception {
		Flux<String> stringFlux1 = rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
		                                        .map(Payload::getDataUtf8)
		                                        .delayElements(Duration.ofMillis(500))
		                                        .take(10)
		                                        .doOnNext(System.out::println);

		StepVerifier.create(stringFlux1)
		            .expectNextCount(10)
		            .verifyComplete();

		System.out.println("going to sleep for 5 seconds...");
		System.out.println("Restarting server!");
		// then Restart Server
		Thread.sleep(5000);

		System.out.println("woke up");

		// reconnection successfully
		// reconnection only when broken connection
		Flux<String> stringFlux2 = rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
		                                        .map(Payload::getDataUtf8)
		                                        .delayElements(Duration.ofMillis(500))
		                                        .take(10)
		                                        .doOnNext(System.out::println);

		StepVerifier.create(stringFlux2)
		            .expectNextCount(10)
		            .verifyComplete();
	}
}
