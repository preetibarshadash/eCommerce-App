package com.preeti.OrderService.config;

import graphql.GraphQLContext;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Component
public class CustomGraphQLContextBuilder implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null) {
            request.configureExecutionInput((input, builder) ->
                    builder.graphQLContext(Map.of("Authorization", authHeader)).build()
            );
        }

        return chain.next(request); // ✅ Must return Mono<WebGraphQlResponse>
    }


}
