package com.example.rsocketspring;

import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.core.Resume;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

// Before run test you must start up first
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties =
		{
				"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
		}
)
public class Lec09SessionResumptionTest {
	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	public void connectionTest() {
		RSocketRequester requester = builder.rsocketConnector(c -> c.reconnect(retryStrategy()) // reconnect ONLY for the NEW REQUEST
		                                                            .resume(resumeStrategy())) // resume activate during session connection fail
		                                    .transport(TcpClientTransport.create("localhost", 6566));

		requester.route("math.service.table")
		         .data(new ComputationRequestDto(5))
		         .retrieveFlux(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1000)
		         .verifyComplete();
	}

	private Retry retryStrategy() {
		return Retry.fixedDelay(100, Duration.ofSeconds(1))
		            .doBeforeRetry(s -> System.out.println("Retrying connection : " + s.totalRetriesInARow()));
	}

	private Resume resumeStrategy() {
		return new Resume().retry(Retry.fixedDelay(300, Duration.ofMillis(500))
		                               .doBeforeRetry(s -> System.out.println("resume -retry: " + s)));
	}
}
