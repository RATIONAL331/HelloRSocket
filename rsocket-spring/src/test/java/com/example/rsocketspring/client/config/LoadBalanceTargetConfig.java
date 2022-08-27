package com.example.rsocketspring.client.config;

import com.example.rsocketspring.client.serviceregistry.RSocketServiceInstance;
import com.example.rsocketspring.client.serviceregistry.ServiceRegistryClient;
import io.rsocket.loadbalance.LoadbalanceTarget;
import io.rsocket.transport.ClientTransport;
import io.rsocket.transport.netty.client.TcpClientTransport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class LoadBalanceTargetConfig {
	private final ServiceRegistryClient serviceRegistryClient;

	@Bean
	public Flux<List<LoadbalanceTarget>> targetFlux() {
		return Flux.from(targets());
	}

	private Mono<List<LoadbalanceTarget>> targets() {
		return Mono.fromCallable(() -> serviceRegistryClient.getInstances()
		                                                    .stream()
		                                                    .map(server -> LoadbalanceTarget.from(key(server), transport(server)))
		                                                    .collect(Collectors.toList()));
	}

	private String key(RSocketServiceInstance socketServiceInstance) {
		return socketServiceInstance.getHost() + ":" + socketServiceInstance.getPort();
	}

	private ClientTransport transport(RSocketServiceInstance rSocketServiceInstance) {
		return TcpClientTransport.create(rSocketServiceInstance.getHost(), rSocketServiceInstance.getPort());
	}
}
