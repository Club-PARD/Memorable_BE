package com.study.memorable.OpenAI.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ChatGPTRequest {

    // Getters and Setters
    private String model;
    private List<Message> messages;

    public ChatGPTRequest() {
        // Default constructor for serialization
    }

    public ChatGPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}
