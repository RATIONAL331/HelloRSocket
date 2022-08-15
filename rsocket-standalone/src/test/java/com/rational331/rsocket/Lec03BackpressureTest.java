package com.rational331.rsocket;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03BackpressureTest {
	private RSocket rSocket;

	@BeforeAll
	public void setup() {
		rSocket = RSocketConnector.create()
		                          .connect(TcpClientTransport.create("localhost", 6565))
		                          .block();
	}

	@Test
	public void backpressure() throws Exception {
		/**
		 * public final class Queues {
		 *     public static final int XS_BUFFER_SIZE = Math.max(8, Integer.parseInt(System.getProperty("reactor.bufferSize.x", "32")));
		 */
		Flux<String> stringFlux = rSocket.requestStream(DefaultPayload.create(""))
		                                 .map(Payload::getDataUtf8)
		                                 .delayElements(Duration.ofMillis(500))
		                                 .doOnNext(System.out::println);

		StepVerifier.create(stringFlux)
		            .expectNextCount(1000)
		            .verifyComplete();
	}
}
