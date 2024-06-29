package com.pard.Memorable.demo.service;

import com.pard.Memorable.demo.dto.ChatGPTRequest;
import com.pard.Memorable.demo.dto.ChatGPTResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAIService {

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    @Autowired
    private RestTemplate restTemplate;

    @Setter
    @Getter
    public List<String> keywords;


    public int getTokenCount(String text) throws Exception {



        // Prepare the request object
        ChatGPTRequest request = new ChatGPTRequest(model, text);

        // Call the OpenAI API
        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        // Get the token count from the response
        return response.getUsage().getPrompt_tokens();
    }
}
