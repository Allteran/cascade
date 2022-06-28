package io.allteran.cascade.workshopservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Collections;

@Configuration
public class WebClientConfig {
    private static final String MANAGE_BASE_URL = "http://localhost:9091";

    @Bean
    public WebClient webClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(MANAGE_BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.URI_COMPONENT);
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(MANAGE_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
