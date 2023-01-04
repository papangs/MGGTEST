package com.mgg;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class SpringBootMybatisApplication {

	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
		return new RestTemplate(getRequestFactory());
	}
	
	@Bean
	public ClientHttpRequestFactory getRequestFactory() {
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
		connectionManager.setMaxTotal(500);
		connectionManager.setDefaultMaxPerRoute(100);

		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(30000)
				.setSocketTimeout(30000)
				.setConnectTimeout(30000).build();
		CloseableHttpClient httpClient = HttpClientBuilder.create().setConnectionManager(connectionManager)
				.setDefaultRequestConfig(config).build();
		return new HttpComponentsClientHttpRequestFactory(httpClient);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootMybatisApplication.class, args);
	}

}
