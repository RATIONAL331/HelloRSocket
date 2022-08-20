package com.example.rsocketspring;

import com.example.rsocketspring.dto.Response;
import io.rsocket.exceptions.ApplicationErrorException;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec03InputValidationTest {
	private RSocketRequester requester;

	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		requester = builder.transport(TcpClientTransport.create("localhost", 6565));
	}

	@Test
	public void validationTest() {
		requester.route("math.validation.double.30")
		         .retrieveMono(Integer.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	/**
	 * SERVER
	 * <p>
	 * java.lang.IllegalArgumentException: can not be > 30
	 * at com.example.rsocketspring.controller.InputValidationController.doubleIt(InputValidationController.java:18) ~[classes/:na]
	 * at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104) ~[na:na]
	 * at java.base/java.lang.reflect.Method.invoke(Method.java:577) ~[na:na]
	 * at org.springframework.messaging.handler.invocation.reactive.InvocableHandlerMethod.lambda$invoke$0(InvocableHandlerMethod.java:137) ~[spring-messaging-5.3.22.jar:5.3.22]
	 * ...
	 * at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]
	 * <p>
	 * =================================================================================
	 * CLIENT
	 * <p>
	 * java.lang.AssertionError: expectation "expectComplete" failed (expected: onComplete(); actual: onError(ApplicationErrorException (0x201): can not be > 30))
	 * <p>
	 * at reactor.test.MessageFormatter.assertionError(MessageFormatter.java:115)
	 * ...
	 * at java.base/java.lang.Thread.run(Thread.java:833)
	 * Suppressed: ApplicationErrorException (0x201): can not be > 30
	 * at io.rsocket.exceptions.Exceptions.from(Exceptions.java:76)
	 * ... 32 more
	 */

	@Test
	public void validationTest2() {
		// client doesn't know what exception exactly;
		// RSocket treats any app exceptions as an [ApplicationErrorException(String message)]!!
		requester.route("math.validation.double.31")
		         .retrieveMono(Integer.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .verifyError(ApplicationErrorException.class);
	}

	// defaultIfEmpty, switchIfEmpty, onErrorReturn
	@Test
	public void defaultIfEmpty() {
		requester.route("math.validation.double.31.defaultIfEmpty")
		         .retrieveMono(Integer.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	public void switchIfEmpty() {
		requester.route("math.validation.double.31.switchIfEmpty")
		         .retrieveMono(Integer.class)
		         .doOnNext(System.out::println) // <- Mono.error occurred (switchIfEmpty)
		         .onErrorReturn(Integer.MIN_VALUE) // <- notice
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	public void exceptionHandler() {
		requester.route("math.exception.handler.double.31")
		         .retrieveMono(Integer.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	public void responseTest() {
		requester.route("math.validation.double.response.31")
		         .retrieveMono(new ParameterizedTypeReference<Response<Integer>>() {
		         })
		         .doOnNext(res -> {
			         if (res.hasError()) {
				         System.out.println(res.getErrorResponse().getStatusCode().getDescription());
			         } else {
				         System.out.println(res.getSuccessResponse());
			         }
		         })
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}
}
