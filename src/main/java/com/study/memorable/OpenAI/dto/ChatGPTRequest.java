package com.study.memorable.OpenAI.dto;

import java.util.ArrayList;
import java.util.List;

public class ChatGPTRequest {

    private String model;
    private List<Message> message;


    public ChatGPTRequest(String model, String prompt){
        this.model = model;
        this.message = new ArrayList<>();
        this.message.add(new Message("user", prompt));
    }



}
