package com.example.userservice;

import com.example.userservice.dto.*;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

import java.util.stream.Stream;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.6.2")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserTransactionTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void init() {
		requester = builder.transport(TcpClientTransport.create("localhost", 9091));
	}

	@Test
	void transactionTest() {
		UserDto randomUser = getRandomUser();
		TransactionRequest transactionRequest = new TransactionRequest(randomUser.getId(), 2000, TransactionType.CREDIT);

		requester.route("user.transaction")
		         .data(transactionRequest)
		         .retrieveMono(TransactionResponse.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextMatches(res -> res.getStatus().equals(TransactionStatus.COMPLETED))
		         .verifyComplete();
	}

	@ParameterizedTest
	@MethodSource("testData")
	void transactionTests(int amount, TransactionType type, TransactionStatus status) {
		UserDto randomUser = getRandomUser();
		TransactionRequest transactionRequest = new TransactionRequest(randomUser.getId(), amount, type);

		requester.route("user.transaction")
		         .data(transactionRequest)
		         .retrieveMono(TransactionResponse.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextMatches(res -> res.getStatus().equals(status))
		         .verifyComplete();
	}

	private UserDto getRandomUser() {
		return requester.route("user.get.all")
		                .retrieveFlux(UserDto.class)
		                .next()
		                .block();
	}

	private Stream<Arguments> testData() {
		return Stream.of(
				Arguments.of(2000, TransactionType.CREDIT, TransactionStatus.COMPLETED),
				Arguments.of(2000, TransactionType.DEBIT, TransactionStatus.COMPLETED),
				Arguments.of(12000, TransactionType.DEBIT, TransactionStatus.FAILED)
		);
	}
}
