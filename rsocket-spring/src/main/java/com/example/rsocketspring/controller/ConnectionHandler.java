package com.example.rsocketspring.controller;

import com.example.rsocketspring.dto.ClientConnectionRequest;
import com.example.rsocketspring.service.MathClientManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ConnectionHandler {
	private final MathClientManager clientManager;

//	implicit Connect Mapping (when you describe override that)
//	@ConnectMapping
//	public Mono<Void> implicitConnectMapping() {
//		return Mono.empty();
//	}

	@ConnectMapping
	public Mono<Void> handleConnection(@Payload(required = false) ClientConnectionRequest request, RSocketRequester requester) {
		System.out.println("connection setup " + request);
		if (Objects.isNull(request)) {
			return Mono.empty();
		}

		if (request.getSecretKey().equals("password")) {
			return Mono.empty();
		}
//		return Mono.error(new RuntimeException("invalid credentials"));
//		return Mono.fromRunnable(() -> requester.rsocketClient().dispose());
		return Mono.empty();
	}

	@ConnectMapping("math.events.connection")
	public Mono<Void> mathEventConnection(RSocketRequester requester) {
		System.out.println("math event connection setup");
		return Mono.fromRunnable(() -> this.clientManager.add(requester));
	}
}
