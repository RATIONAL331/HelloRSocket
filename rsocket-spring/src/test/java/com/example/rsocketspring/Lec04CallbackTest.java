package com.example.rsocketspring;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec04CallbackTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@Autowired
	private RSocketMessageHandler handler; // <- Bind Test package @MessageMapping("batch.job.response") in BatchJobResponseController

	@BeforeAll
	public void setup() {
		requester = builder.rsocketConnector(c -> c.acceptor(handler.responder()))
		                   .transport(TcpClientTransport.create("localhost", 6565));
	}

	@Test
	public void callbackTest() throws InterruptedException {
		requester.route("batch.job.request")
		         .data(5)
		         .send()
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .verifyComplete();
		Thread.sleep(1000);
	}

}
