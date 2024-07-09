package com.study.memorable.OpenAI.service;

import com.study.memorable.OpenAI.dto.ChatGPTRequest;
import com.study.memorable.OpenAI.dto.ChatGPTResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpenAIService {

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> extractKeywordsFromText(int len, String text) {
        String promptKeywords = Prompts.extractKeywordsPrompt(len, text);

        ChatGPTRequest request = new ChatGPTRequest(model, promptKeywords);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);

        log.info("Calling OpenAI API with prompt for keywords");
        ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);
        log.info("Received response from OpenAI API for keywords: {}", response);

        assert response != null;
        return response.getChoices().stream()
                .flatMap(choice -> extractKeywordsFromMessage(choice.getMessage().getContent()).stream())
                .distinct()
                .limit(len)
                .collect(Collectors.toList());
    }

    public Map<String, List<String>> processKeywords(List<String> keywords, String text) {
        String prompt = Prompts.buildPrompt(keywords, text);
        String response = callOpenAI(prompt);
        log.info("Received response from OpenAI API for questions and answers: \n{}", response);
        return parseResponseToQuestionsAndAnswers(response);
    }

    private List<String> extractKeywordsFromMessage(String content) {
        return Arrays.stream(content.split(",\\s*"))
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .collect(Collectors.toList());
    }

    private String callOpenAI(String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);

        log.info("Calling OpenAI API with prompt for questions and answers");
        ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);

        assert response != null;
        return response.getChoices().get(0).getMessage().getContent();
    }

    private Map<String, List<String>> parseResponseToQuestionsAndAnswers(String response) {
        String[] lines = response.split("\n");
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith("Q: ")) {
                String question = lines[i].replace("Q: ", "").trim();
                if (i + 1 < lines.length && lines[i + 1].startsWith("A: ")) {
                    String answer = lines[i + 1].replace("A: ", "").trim();
                    questions.add(question);
                    answers.add(answer);
                    i++; // Skip next line since it's the answer line
                }
            }
        }

        log.info("Parsed questions: {}", questions);
        log.info("Parsed answers: {}", answers);

        Map<String, List<String>> result = new HashMap<>();
        result.put("questions", questions);
        result.put("answers", answers);
        return result;
    }

    public Map<String, Object> scoreAnswers(String content, List<String> questions, List<String> answers, List<String> userAnswers) {
        String prompt = Prompts.scoreAnswersPrompt(content, questions, answers, userAnswers);

        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);

        log.info("Calling OpenAI API for scoring answers");
        ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);
        log.info("Received response from OpenAI API for scoring answers: {}", response);

        assert response != null;
        String result = response.getChoices().get(0).getMessage().getContent();

        return parseScoreResponse(result);
    }

    private Map<String, Object> parseScoreResponse(String response) {
        Map<String, Object> result = new HashMap<>();

        // 전체 응답 문자열에서 <>를 제거하고, 괄호 내의 내용을 분할
        String cleanedResponse = response.replace("<", "").replace(">", "").trim();
        String[] parts = cleanedResponse.split("\\[|\\]");
        log.info("\n\n\n\nresult: " + Arrays.toString(parts));

        // 첫 번째 부분을 score로 설정
        result.put("score", Integer.parseInt(parts[0].trim().split(",")[0]));
        log.info("parts[0]: " + parts[0]);

        for(int i = 0; i < parts.length; i++)
            log.info("parts: " + parts[i]);

        // 두 번째 부분을 isCorrect로 설정
        String[] boolValues = parts[1].split(", ");
        List<Boolean> isCorrectList = Arrays.stream(boolValues)
                .map(Boolean::parseBoolean)
                .collect(Collectors.toList());
        result.put("isCorrect", isCorrectList);

        log.info("isCorrectList: " + isCorrectList);
        return result;
    }

}
