package com.example.stockservice;

import com.example.stockservice.dto.StockTickDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@SpringBootTest
class StockServiceApplicationTests {
	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	void contextLoads() {
	}

	@Test
	void stockPriceTest() {
		RSocketRequester requester = builder.transport(TcpClientTransport.create("localhost", 9090));
		requester.route("stock.ticks")
		         .retrieveFlux(StockTickDto.class)
		         .take(12)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(12)
		         .verifyComplete();
	}

}
