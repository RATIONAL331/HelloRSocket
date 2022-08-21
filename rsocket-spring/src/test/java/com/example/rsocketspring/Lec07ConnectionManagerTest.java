package com.example.rsocketspring;

import com.example.rsocketspring.dto.ClientConnectionRequest;
import com.example.rsocketspring.dto.ComputationRequestDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Lec07ConnectionManagerTest {
	@Autowired
	private RSocketRequester.Builder builder;

	private final ClientConnectionRequest clientConnectionRequest = new ClientConnectionRequest();

	@BeforeEach
	public void setup() {
		clientConnectionRequest.setClientId("order-service");
		clientConnectionRequest.setSecretKey("asdf");
	}

	/**
	 * [] <- initial set is empty
	 * connection setup ClientConnectionRequest(clientId=order-service, secretKey=asdf)
	 * connection setup ClientConnectionRequest(clientId=order-service, secretKey=asdf)
	 * ComputationRequestDto(input=5)
	 * ComputationRequestDto(input=5)
	 * [org.springframework.messaging.rsocket.DefaultRSocketRequester@2c210e17, org.springframework.messaging.rsocket.DefaultRSocketRequester@530a6253]
	 * [org.springframework.messaging.rsocket.DefaultRSocketRequester@2c210e17, org.springframework.messaging.rsocket.DefaultRSocketRequester@530a6253]
	 * [org.springframework.messaging.rsocket.DefaultRSocketRequester@2c210e17, org.springframework.messaging.rsocket.DefaultRSocketRequester@530a6253]
	 * [org.springframework.messaging.rsocket.DefaultRSocketRequester@2c210e17, org.springframework.messaging.rsocket.DefaultRSocketRequester@530a6253]
	 * [org.springframework.messaging.rsocket.DefaultRSocketRequester@2c210e17, org.springframework.messaging.rsocket.DefaultRSocketRequester@530a6253]
	 * finally closed
	 * finally closed
	 */

	@Test
	public void connectionTest() throws InterruptedException {
		RSocketRequester requester1 = builder.setupData(clientConnectionRequest)
		                                     .transport(TcpClientTransport.create("localhost", 6565));

		RSocketRequester requester2 = builder.setupData(clientConnectionRequest)
		                                     .transport(TcpClientTransport.create("localhost", 6565));

		requester1.route("math.service.print").data(new ComputationRequestDto(5)).send().subscribe();
		requester2.route("math.service.print").data(new ComputationRequestDto(5)).send().subscribe();

		Thread.sleep(5000);
	}
}
