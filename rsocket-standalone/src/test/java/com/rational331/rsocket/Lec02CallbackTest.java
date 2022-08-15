package com.rational331.rsocket;

import com.rational331.rsocket.client.CallbackService;
import com.rational331.rsocket.dto.RequestDto;
import com.rational331.rsocket.utils.ObjectUtil;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec02CallbackTest {
	private RSocket rSocket;

	@BeforeAll
	public void setup() {
		rSocket = RSocketConnector.create()
		                          .acceptor(SocketAcceptor.with(new CallbackService()))
		                          .connect(TcpClientTransport.create("localhost", 6565))
		                          .block();
	}

	@Test
	public void callback() throws Exception {
		RequestDto requestDto = new RequestDto(5);
		Mono<Void> voidMono = rSocket.fireAndForget(ObjectUtil.toPayload(requestDto));

		StepVerifier.create(voidMono)
		            .verifyComplete();

		System.out.println("Waiting for callback...");

		Thread.sleep(3000);
	}
}
