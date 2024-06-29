package com.pard.Memorable.demo.service;

import com.pard.Memorable.demo.dto.ChatGPTRequest;
import com.pard.Memorable.demo.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    @Autowired
    private RestTemplate restTemplate;

    public int getTokenCount(String text) throws Exception {
        // Prepare the request object
        ChatGPTRequest request = new ChatGPTRequest(model, text);

        // Call the OpenAI API
        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        // Get the token count from the response
        return response.getUsage().getPrompt_tokens();
    }
}
