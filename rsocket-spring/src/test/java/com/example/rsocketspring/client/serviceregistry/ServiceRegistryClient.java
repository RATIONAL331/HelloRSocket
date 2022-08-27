package com.example.rsocketspring.client.serviceregistry;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Getter
public class ServiceRegistryClient {
	private final List<RSocketServiceInstance> instances;

	public ServiceRegistryClient() {
		this.instances = Arrays.asList(
				new RSocketServiceInstance("localhost", 7071),
				new RSocketServiceInstance("localhost", 7072),
				new RSocketServiceInstance("localhost", 7073)
		);
	}
}
