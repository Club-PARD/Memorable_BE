package com.study.memorable.File.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.service.FileService;
import com.study.memorable.OpenAI.dto.ChatGPTRequest;
import com.study.memorable.OpenAI.dto.ChatGPTResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

//    @Value("${openai.model}")
//    private String model;
//
//    @Value("${openai.api.url}")
//    private String apiURL;

//    @Value("${openai.api.key}")
//    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("")
    public String createFile(@RequestBody FileCreateDTO dto) {
        fileService.createFile(dto);
        return "파일 생성됨!";
    }

    @GetMapping("")
    public List<FileReadDTO> findAll() {
        return fileService.findAll();
    }
}