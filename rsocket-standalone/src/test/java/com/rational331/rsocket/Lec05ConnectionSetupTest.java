package com.rational331.rsocket;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec05ConnectionSetupTest {
	private RSocketClient rSocketClient;

	@BeforeAll
	public void setup() {
		Mono<RSocket> rSocketMono = RSocketConnector.create()
		                                            .setupPayload(DefaultPayload.create("user:password"))
		                                            .connect(TcpClientTransport.create("localhost", 6565))
		                                            .doOnNext(r -> System.out.println("going to connect"));

		this.rSocketClient = RSocketClient.from(rSocketMono);
	}

	@Test
	public void setupPayloadTest() throws Exception {
		Payload payload = ObjectUtil.toPayload(new RequestDto(5));

		Flux<ResponseDto> responseDtoFlux = rSocketClient.requestStream(Mono.just(payload))
		                                                 .map(p -> ObjectUtil.toObject(p, ResponseDto.class))
		                                                 .doOnNext(System.out::println);

		StepVerifier.create(responseDtoFlux)
		            .expectNextCount(10)
		            .verifyComplete();
	}
}
