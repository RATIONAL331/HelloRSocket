package com.example.rsocketspring;

import com.example.rsocketspring.dto.ClientConnectionRequest;
import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.nio.channels.ClosedChannelException;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06ConnectionSetupTest2 {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		ClientConnectionRequest clientConnectionRequest = new ClientConnectionRequest();
		clientConnectionRequest.setClientId("order-service");
		clientConnectionRequest.setSecretKey("asdf");
		requester = builder.setupData(clientConnectionRequest)
		                   .transport(TcpClientTransport.create("localhost", 6565));
	}


	/** return Mono.error()
	 * connection setup ClientConnectionRequest(clientId=order-service, secretKey=asdf)
	 * ERROR 63706 --- [ctor-http-nio-3] o.s.m.h.i.reactive.InvocableHelper       : No exception handling method
	 * java.lang.RuntimeException: invalid credentials
	 */

	/**
	 * return Mono.fromCallable(() -> requester.rsocketClient().dispose())
	 * connection setup ClientConnectionRequest(clientId=order-service, secretKey=asdf)
	 */

	@Test
	public void connectionTest() {
		requester.route("math.service.square")
		         .data(new ComputationRequestDto(ThreadLocalRandom.current().nextInt(1, 50)))
		         .retrieveMono(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
//		         .verifyError(RejectedSetupException.class); // <- Clients get RejectedSetupException despite RuntimeException <- return Mono.error(new RuntimeException(...));
                 .verifyError(ClosedChannelException.class); // <- Clients get ClosedChannelException despite RuntimeException <- return return Mono.fromRunnable(() -> requester.rsocketClient().dispose());
	}
}
