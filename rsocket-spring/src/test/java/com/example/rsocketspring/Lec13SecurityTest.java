package com.example.rsocketspring;

import com.example.rsocketspring.dto.ComputationRequestDto;
import com.example.rsocketspring.dto.ComputationResponseDto;
import io.rsocket.metadata.WellKnownMimeType;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.rsocket.metadata.UsernamePasswordMetadata;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import reactor.test.StepVerifier;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Lec13SecurityTest {
	private final MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.MESSAGE_RSOCKET_AUTHENTICATION.getString());
	private RSocketRequester requester;
	@Autowired
	private RSocketRequester.Builder builder;

	@BeforeAll
	public void setup() {
		UsernamePasswordMetadata usernamePasswordMetadata = new UsernamePasswordMetadata("client", "password");

		requester = builder.setupMetadata(usernamePasswordMetadata, mimeType)
		                   .transport(TcpClientTransport.create("localhost", 6565));
	}

	@Test
	public void requestResponse() {
		UsernamePasswordMetadata credentials = new UsernamePasswordMetadata("user", "password");
		requester.route("math.service.secured.square")
		         .metadata(credentials, mimeType)
		         .data(new ComputationRequestDto(5))
		         .retrieveMono(ComputationResponseDto.class)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(1)
		         .verifyComplete();
	}

	@Test
	public void requestStream() {
		UsernamePasswordMetadata credentials = new UsernamePasswordMetadata("admin", "password");
		requester.route("math.service.secured.table")
		         .metadata(credentials, mimeType)
		         .data(new ComputationRequestDto(5))
		         .retrieveFlux(ComputationResponseDto.class)
		         .take(10)
		         .doOnNext(System.out::println)
		         .as(StepVerifier::create)
		         .expectNextCount(10)
		         .verifyComplete();
	}

}
