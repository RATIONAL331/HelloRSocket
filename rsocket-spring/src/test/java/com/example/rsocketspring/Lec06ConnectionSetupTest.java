package com.example.rsocketspring;

import com.example.rsocketspring.dto.ClientConnectionRequest;
import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec06ConnectionSetupTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		ClientConnectionRequest clientConnectionRequest = new ClientConnectionRequest();
		clientConnectionRequest.setClientId("order-service");
		clientConnectionRequest.setSecretKey("password");
		requester = builder.setupData(clientConnectionRequest)
		                   .transport(TcpClientTransport.create("localhost", 6565));
	}


	/**
	 * connection setup ClientConnectionRequest(clientId=order-service, secretKey=password)
	 * ComputationResponseDto(input=8, output=64)
	 * ComputationResponseDto(input=1, output=1)
	 * ComputationResponseDto(input=7, output=49)
	 */

	@RepeatedTest(3)
	public void connectionTest() {
		requester.route("math.service.square")
		         .data(new ComputationRequestDto(ThreadLocalRandom.current().nextInt(1, 50)))
		         .retrieveMono(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}
}
