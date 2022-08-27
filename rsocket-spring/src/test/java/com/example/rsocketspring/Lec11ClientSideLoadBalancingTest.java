package com.example.rsocketspring;

import com.example.rsocketspring.dto.ComputationRequestDto;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.loadbalance.WeightedLoadbalanceStrategy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties =
		{
				"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.rsocket.RSocketServerAutoConfiguration"
		}
)
public class Lec11ClientSideLoadBalancingTest {
	@Autowired
	private Flux<List<LoadbalanceTarget>> targets;
	@Autowired
	private RSocketRequester.Builder builder;

	@Test
	public void clientSide() throws InterruptedException {
		//RSocketRequester requester = builder.transports(targets, new RoundRobinLoadbalanceStrategy());
		RSocketRequester requester = builder.transports(targets, WeightedLoadbalanceStrategy.create());
		for (int i = 0; i < 50; i++) {
			requester.route("math.service.print").data(new ComputationRequestDto(i)).send().subscribe();
			Thread.sleep(1000);
		}
	}
}
