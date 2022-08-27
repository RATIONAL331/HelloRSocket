package com.example.rsocketspring.security;

import org.springframework.boot.rsocket.messaging.RSocketStrategiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.messaging.handler.invocation.reactive.AuthenticationPrincipalArgumentResolver;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.metadata.SimpleAuthenticationEncoder;

@Configuration
@EnableRSocketSecurity
@EnableMethodSecurity
public class RSocketSecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RSocketStrategiesCustomizer rSocketStrategiesCustomizer() {
		return c -> c.encoder(new SimpleAuthenticationEncoder());
	}

	@Bean
	public RSocketMessageHandler rSocketMessageHandler(RSocketStrategies rSocketStrategies) {
		RSocketMessageHandler handler = new RSocketMessageHandler();
		handler.setRSocketStrategies(rSocketStrategies);
		handler.getArgumentResolverConfigurer().addCustomResolver(new AuthenticationPrincipalArgumentResolver()); // @AuthenticationPrincipal Mono<UserDetails>
		return handler;
	}

	@Bean
	public PayloadSocketAcceptorInterceptor interceptor(RSocketSecurity security) {
		return security.simpleAuthentication(Customizer.withDefaults())
		               .authorizePayload(authorize -> authorize.setup()
//		                                                       .permitAll()
                                                               .hasRole("TRUSTED_CLIENT") // onError(RejectedSetupException (0x3): Access Denied))
//                                                               .anyRequest()
//                                                               .permitAll()
//                                                               .hasRole("USER")
//                                                               .route("math.service.secured.table")
//                                                               .hasRole("ADMIN")
//                                                               .route("*.*.*.square")
//                                                               .hasRole("USER")
                                                               .anyRequest()
//                                                               .denyAll()
                                                               .authenticated()
		               )
		               .build();
	}
}
