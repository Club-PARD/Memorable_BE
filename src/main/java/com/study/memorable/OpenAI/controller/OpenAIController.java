package com.study.memorable.OpenAI.controller;

import com.study.memorable.OpenAI.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class OpenAIController {

    private final OpenAIService openAIService;

    @Autowired
    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/extract-keywords")
    public List<String> extractKeywordsFromContent(@RequestBody String text) {
        int textLengthWithoutSpaces = text.replace(" ", "").replace("\n", "").length();
        int len = (int) (textLengthWithoutSpaces * 0.03);
//        int len = 10; // 키워드 개수
        log.info("Text length without spaces: {}", len);
        return openAIService.extractKeywordsFromText(len, text);
    }

    @PostMapping("/process-keywords")
    @SuppressWarnings("unchecked")
    public Map<String, List<String>> processKeywords(@RequestBody Map<String, Object> requestBody) {
        List<String> keywords = (List<String>) requestBody.get("keywords");
        String text = (String) requestBody.get("text");
        return openAIService.processKeywords(keywords, text);
    }

    @PostMapping("/score-answers")
    @SuppressWarnings("unchecked")
    public Map<String, Object> scoreAnswers(@RequestBody Map<String, Object> requestBody) {
        String content = (String) requestBody.get("content");
        List<String> questions = (List<String>) requestBody.get("questions");
        List<String> answers = (List<String>) requestBody.get("answers");
        List<String> userAnswers = (List<String>) requestBody.get("userAnswers");
        return openAIService.scoreAnswers(content, questions, answers, userAnswers);
    }

}
