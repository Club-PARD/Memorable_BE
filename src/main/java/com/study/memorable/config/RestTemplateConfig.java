package com.study.memorable.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Value("${openai.api.key}")
    private String openAiKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));

        // Add the authorization header
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors == null) {
            restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + openAiKey);
                return execution.execute(request, body);
            }));
        } else {
            interceptors.add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer " + openAiKey);
                return execution.execute(request, body);
            });
            restTemplate.setInterceptors(interceptors);
        }

        return restTemplate;
    }
}
