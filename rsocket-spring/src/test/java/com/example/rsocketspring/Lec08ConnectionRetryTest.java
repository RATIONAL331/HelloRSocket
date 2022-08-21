package com.example.rsocketspring;

import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
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
public class Lec08ConnectionRetryTest {
	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	public void connectionTest() throws InterruptedException {
		RSocketRequester requester = builder.rsocketConnector(c -> c.reconnect(Retry.fixedDelay(10, Duration.ofSeconds(1))
		                                                                            .doBeforeRetry(s -> System.out.println("retrying: " + s.totalRetriesInARow()))))
		                                    .transport(TcpClientTransport.create("localhost", 6565));

		for (int i = 0; i < 25; i++) {
			requester.route("math.service.square")
			         .data(new ComputationRequestDto(i))
			         .retrieveMono(ComputationResponseDto.class)
			         .doOnNext(System.out::println)
			         .as(StepVerifier::create)
			         .expectNextCount(1)
			         .verifyComplete();

			Thread.sleep(300);
		}
	}

}
