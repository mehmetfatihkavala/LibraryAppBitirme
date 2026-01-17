package com.kavala.loan_service.infrastructure.adapter.http.member;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * Configuration for MemberClient.
 */
@Configuration
public class MemberClientConfig {

    @Value("${loan-service.member-service.url:http://member-service}")
    private String memberServiceUrl;

    @Bean
    public MemberClient memberClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(memberServiceUrl)
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(MemberClient.class);
    }
}
