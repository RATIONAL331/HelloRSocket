package com.example.userservice.config;

import com.example.userservice.dto.OperationType;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketServiceConfig {
	@Bean
	public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
		MimeType mimeType = MimeTypeUtils.parseMimeType(WellKnownMimeType.APPLICATION_CBOR.getString());
		return c -> c.metadataExtractorRegistry(r -> r.metadataToExtract(mimeType, OperationType.class, "operation-type"));
	}
}
