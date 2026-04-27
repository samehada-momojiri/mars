package com.nine2nine.app.mars.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.nine2nine.app.mars.client.JupiterTaskClient;

@Configuration
public class JupiterClientConfig {

	@Bean
	public JupiterTaskClient jupiterTaskClient(
			@Value("${jupiter.base-url:http://localhost:8081}") String jupiterBaseUrl) {
		RestClient restClient = RestClient.builder().baseUrl(jupiterBaseUrl).build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
				.build();
		return factory.createClient(JupiterTaskClient.class);
	}
}
