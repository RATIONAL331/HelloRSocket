package com.rational331.rsocket.service;

import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import reactor.core.publisher.Mono;

public class SocketAcceptorImpl implements SocketAcceptor {
	@Override
	public Mono<RSocket> accept(ConnectionSetupPayload connectionSetupPayload, RSocket rSocket) { // <- client rsocket
		System.out.println("SocketAcceptorImpl - Accept Method");
		return Mono.fromCallable(MathService::new); // server rsocket
	}
}
