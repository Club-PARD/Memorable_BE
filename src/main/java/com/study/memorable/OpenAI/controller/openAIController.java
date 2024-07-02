package com.study.memorable.OpenAI.controller;

import com.study.memorable.OpenAI.dto.TextRequest;
import com.study.memorable.OpenAI.dto.TextResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/api")
public class openAIController {

//    @Value("${openai.model}")
//    private String model;
//
//    @Value("${openai.api.url}")
//    private String apiURL;
//
//    @Value("${openai.api.key}")
//    private String apiKey;

//    @Autowired
//    private RestTemplate restTemplate;

//    @Autowired
//    private OpenAIService openAIService;

    @PostMapping("/getContent")
    public TextResponse getContent(@RequestBody TextRequest request) {
        String text = request.getText();
        return new TextResponse(text);
    }
}


