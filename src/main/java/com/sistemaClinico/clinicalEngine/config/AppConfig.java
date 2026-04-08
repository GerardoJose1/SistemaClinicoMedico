package com.sistemaClinico.clinicalEngine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(5)) // Tiempo máximo para establecer conexión
                .readTimeout(Duration.ofSeconds(10)) // Tiempo máximo para esperar los datos
                .errorHandler(new DefaultResponseErrorHandler())
                .build();
    }

    @Bean("wordpressWebClient")
    public WebClient wordpressWebClient(
            @Value("${wordpress.base-url}") String baseUrl,
            @Value("${wordpress.username}") String username,
            @Value("${wordpress.app-password}") String password
    ) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders( h -> h.setBasicAuth(username, password.replace(" ", "")))
                .codecs(c -> c.defaultCodecs().maxInMemorySize( 2 * 1024 * 1024))
                .build();

    }

}