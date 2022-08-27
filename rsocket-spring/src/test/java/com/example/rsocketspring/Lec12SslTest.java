package com.example.rsocketspring;

import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.netty.tcp.TcpClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Lec12SslTest {
	static {
		System.setProperty("javax.net.ssl.trustStore", "/Users/nhn/Documents/GitHub_Private/HelloRSocket/ssl-tls/client.truststore");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");
	}

	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	public void sslTlsTest() {
		RSocketRequester requester = builder.transport(TcpClientTransport.create(TcpClient.create().host("localhost").port(6565).secure()));
		requester.route("math.service.square")
		         .data(new ComputationRequestDto(5))
		         .retrieveMono(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}
}
