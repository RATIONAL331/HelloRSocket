package com.rational331.rsocket.service;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import reactor.core.publisher.Mono;

public class SocketAcceptorImpl implements SocketAcceptor {
	@Override
	public Mono<RSocket> accept(ConnectionSetupPayload connectionSetupPayload, RSocket rSocket) { // <- param rSocket is client rsocket
		System.out.println("SocketAcceptorImpl - Accept Method");
//		Mono.fromCallable(MathService::new);
//		Mono.fromCallable(() -> new BatchJobService(rSocket)); // <- param rSocket is client rsocket
		if (isValidClient(connectionSetupPayload.getDataUtf8())) {
			return Mono.fromCallable(MathService::new);
		}
		return Mono.fromCallable(FreeService::new); // return server rsocket
	}

	private boolean isValidClient(String credentials) {
		return "user:password".equals(credentials);
	}
}
