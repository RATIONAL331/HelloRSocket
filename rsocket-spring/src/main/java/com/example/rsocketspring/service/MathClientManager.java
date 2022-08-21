package com.example.rsocketspring.service;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class MathClientManager {
	private final Set<RSocketRequester> set = Collections.synchronizedSet(new HashSet<>());

	public void add(RSocketRequester requester) {
		requester.rsocket()
		         .onClose()
		         .doFirst(() -> set.add(requester))
		         .doFinally(s -> {
			         System.out.println("finally closed");
			         set.remove(requester);
		         })
		         .subscribe();
	}

	@Scheduled(fixedDelay = 1000)
	public void print() {
		System.out.println(set);
	}

	public void notify(int i) {
		Flux.fromIterable(set)
		    .flatMap(r -> r.route("math.updates")
		                   .data(i)
		                   .send())
		    .subscribe();
	}


}
