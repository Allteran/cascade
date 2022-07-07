package io.allteran.cascade.apigateway.config;

import io.allteran.cascade.apigateway.dto.EmployeeDTO;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing auth information");
            }

            String authHeader = exchange.getRequest().getHeaders().get(org.springframework.http.HttpHeaders.AUTHORIZATION).get(0);
            String[] parts = authHeader.split(" ");

            if(parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect auth structure");
            }

            return webClientBuilder.build()
                    .post()
                    .uri("http://manager-service/api/v1/auth/validateToken?token=" + parts[1])
                    .retrieve()
                    .bodyToMono(EmployeeDTO.class)
                    .map(user -> {
                        exchange.getRequest()
                                .mutate()
                                .header("x-auth-user-id", user.getId());
                        return exchange;
                    }).flatMap(chain::filter);

        };
    }

    public static class Config {
        //live it empty because we dont need any particular configuration
    }
}
