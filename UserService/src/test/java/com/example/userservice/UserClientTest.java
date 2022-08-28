package com.example.userservice;

import com.example.userservice.dto.OperationType;
import com.example.userservice.dto.UserDto;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.test.StepVerifier;

@SpringBootTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.6.2")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserClientTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void init() {
		requester = builder.transport(TcpClientTransport.create("localhost", 9091));
	}

	@Test
	void getAllUsers() {
		requester.route("user.get.all")
		         .retrieveFlux(UserDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(3)
		         .verifyComplete();
	}

	@Test
	void getSingleUser() {
		UserDto randomUser = this.getRandomUser();

		requester.route("user.get.{id}", randomUser.getId())
		         .retrieveMono(UserDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextMatches(dto -> dto.getId().equals(randomUser.getId()))
		         .verifyComplete();
	}

	@Test
	void postUserTest() {
		UserDto userDto = new UserDto();
		userDto.setName("test");
		userDto.setBalance(100);

		requester.route("user.create")
		         .data(userDto)
		         .retrieveMono(UserDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	void putUserTest() {
		UserDto randomUser = getRandomUser();
		randomUser.setBalance(-10);

		requester.route("user.update.{id}", randomUser.getId())
		         .data(randomUser)
		         .retrieveMono(UserDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextMatches(d -> d.getBalance() == -10)
		         .verifyComplete();
	}

	@Test
	void deleteUser() throws InterruptedException {
		UserDto randomUser = getRandomUser();

		requester.route("user.delete.{id}", randomUser.getId())
		         .send()
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .verifyComplete();

		Thread.sleep(1000);

		requester.route("user.get.all")
		         .retrieveFlux(UserDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(2)
		         .verifyComplete();
	}

	@Test
	void metadataTest() {
		MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.APPLICATION_CBOR.getString());

		UserDto userDto = new UserDto();
		userDto.setName("test");
		userDto.setBalance(100);

		/**
		 * PUT
		 * UserDto(id=null, name=test, balance=100)
		 */

		requester.route("user.operation.type")
		         .metadata(OperationType.PUT, mimeType)
		         .data(userDto)
		         .send()
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	private UserDto getRandomUser() {
		return requester.route("user.get.all")
		                .retrieveFlux(UserDto.class)
		                .next()
		                .block();
	}

}
