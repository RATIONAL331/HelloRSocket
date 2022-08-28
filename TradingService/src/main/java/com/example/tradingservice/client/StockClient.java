package com.example.tradingservice.client;

import com.example.tradingservice.dto.stock.StockTickDto;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketConnectorConfigurer;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StockClient {
	private final RSocketRequester requester;
	private Flux<StockTickDto> flux;
	private final Map<String, Integer> map;

	public StockClient(RSocketRequester.Builder builder,
	                   RSocketConnectorConfigurer connectorConfigurer,
	                   @Value("${stock.service.host}") String host,
	                   @Value("${stock.service.port}") int port) {
		this.requester = builder.rsocketConnector(connectorConfigurer)
		                        .transport(TcpClientTransport.create(host, port));
		this.map = new ConcurrentHashMap<>();
		this.initialize();
	}

	public Flux<StockTickDto> getStockStream() {
		return flux;
	}

	public int getCurrentStockPrice(String stockSymbol) {
		return map.getOrDefault(stockSymbol, 0);
	}

	private void initialize() {
		this.flux = requester.route("stock.ticks")
		                     .retrieveFlux(StockTickDto.class)
		                     .doOnNext(s -> map.put(s.getCode(), s.getPrice()))
		                     .retryWhen(Retry.fixedDelay(Long.MAX_VALUE, Duration.ofSeconds(2)))
		                     .publish()
		                     .autoConnect(0);
	}
}
