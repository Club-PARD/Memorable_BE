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
        String promptKeywords = String.format(
                "### Situation ###\n" +
                        "너는 학생들에게 학습을 도와주는 선생님이야.\n" +
                        "너의 전공은 텍스트 주제에 맞는 전공이야. 예를 들어 텍스트가 사회학을 주제로 한다면 너는 사회학 교수야.\n" +
                        "학생들에게 텍스트 범위 내에서 시험 문제를 출제하기 위해서 문맥 상 중요한 키워드를 먼저 추출하려고 해.\n" +
                        "문제 없이 키워드만 보내주면 돼\n" +
                        "### Instruction ###\n" +
                        "Extract %d important keywords from the text below:\n" +
                        "1. Keywords must be proper nouns and represent core concepts.\n" +
                        "2. Remove particles, case markers, punctuation and spaces.\n" +
                        "3. Exclude suffixes or adjective ending or Prepositional particles attached to proper nouns. For example, '컴퓨터는' should be '컴퓨터'.\n" +
                        "4. Do not translate foreign words. Keep them as they appear in the text.\n" +
                        "5. List keywords by importance without numbering. Importance criterion to understanding the text: uncommon words, strongly related to the text's theme, etc.\n" +
                        "6. Ensure no duplicate keywords. If duplicates are found, replace them with other unique keywords to meet the required count.\n" +
                        "7. Provide exactly %d unique keywords in one proper noun form each.\n" +
                        "8. If a keyword contains multiple words (e.g., '자크 랑시에르'), split them into individual keywords (e.g., '자크', '랑시에르').\n" +
                        "### Text ###\n" +
                        "%s",
                len, len, text
        );

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
        String prompt = buildPrompt(keywords, text);
        String response = callOpenAI(prompt);
        log.info("Received response from OpenAI API for questions and answers: \n{}", response);
        return parseResponseToQuestionsAndAnswers(response);
    }

    private List<String> extractKeywordsFromMessage(String content) {
        return Arrays.stream(content.split(",\\s*"))
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .collect(Collectors.toList());
    }

    private String buildPrompt(List<String> keywords, String text) {
        String keywordList = String.join(", ", keywords);
        return String.format(
                "### Situation ###\n" +
                        "너는 학생들에게 학습을 도와주는 선생님이야.\n" +
                        "너의 전공은 텍스트 주제에 맞는 전공이야. 예를 들어 텍스트가 사회학을 주제로 한다면 너는 사회학 교수야.\n" +
                        "학생들에게 텍스트 범위 내에서 시험 문제를 출제해야 해. 문제의 답이 될 수 있는 문맥 상 중요한 키워드를 먼저 추출했어: %s\n" +
                        "### Instruction ###\n" +
                        "Based on the provided text, generate 20 questions focusing on key concepts.\n" +
                        "The purpose is to verify understanding of the text by learners.\n" +
                        "### Format ###\n" +
                        "1. Question: Formulate a question that allows the key concept (answer) to be inferred. Each questions' answer should be selected from the given list: [%s] Questions should be of a difficulty level suitable for university students.\n" +
                        "문제의 난이도: 대학원생, 교수에 맞게 2. Answer: You must select answers from the provided keywords: %s\n" +
                        "Answers should not be duplicated. Use the exact form of the words in the list as the answer without any changes.\n" +
                        "### Example ###\n" +
                        "Text: '기독교가 역사에서 살아남음=기독교가 상황에 잘 적응하느냐?'\n" +
                        "Q: 기독교가 역사에서 살아남은 것은 무엇과 관련이 있는가?\n" +
                        "A: 상황 적응\n" +
                        "Text: (컴퓨터가 사람에게 도움을 주는 텍스트의 일부 내용이라고 가정)\n" +
                        "Q: 사람에게 도움을 준 도구로 언급된 것은?\n" +
                        "A: 컴퓨터\n" +
                        "### 금지 형식 ###\n" +
                        "문장 형식은 절대 금지한다. 오로지 고유 명사 형태이어야 한다\n" +
                        "문제를 생성할 때 '~ 중에서 하나는 무엇인가?'와 같이 여러 개의 답이 가능하면서 하나의 답만을 정답으로 인정하는 문제는 금지한다.\n" +
                        "### Guidelines ###\n" +
                        "1. Answers must not include particles.\n" +
                        "2. Questions and answers should not include any additional explanations or comments.\n" +
                        "3. Questions must not contain the answer. Words that belong to the answers must never be included in the questions.\n" +
                        "4. Ensure the quality of questions is excellent.\n" +
                        "5. Each question must clearly lead to a single, specific answer without ambiguity.\n" +
                        "6. Answers must not be duplicated. If duplicates are found, generate new questions and answers.\n" +
                        "7. Output can be in both Korean and English.\n" +
                        "8. If the text is code, generate questions related to programming syntax.\n" +
                        "9. Extracted questions should be in Korean.\n" +
                        "10. 문제를 생성할 때, 정답이 특정 카테고리 안에서 고를 수 있는 정답이라면, 문제에서 그 카테고리를 언급해줘. 물론, 그 문제 내에서 정답이 직접적으로 언급되면 안돼.\n" +
                        "출력 형식: 질문은 Q: 로 시작하고, 답변은 A: 로 시작해야 한다. 질문과 답변은 각각 개행없이 연속으로 작성되어야 한다. ##문제 앞에 번호 절대 붙이지 마!!!## (문제+답)의 리스트 정답은 중복되면 안돼. 정답은 무조건 이 안에서 추출해야 해 : %s\n" +
                        "### Text ###\n" +
                        "%s",
                keywordList, keywordList, keywordList, keywordList, text
        );
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
        String prompt = String.format(
                "Please grade the following set of 20 questions and answers. " +
                        "Read the content and understand its context. " +
                        "Compare each answer and user answer against the content and questions. " +
                        "If the user's answer is contextually similar to the actual answer based on the content and questions, mark it as correct. " +
                        "Increment the score by 1 for each correct answer and change the corresponding index in isCorrect to true.\n\n" +
                        "Content: %s\n\n" +
                        "Questions and Answers:\n\n",
                content
        );

        for (int i = 0; i < questions.size(); i++) {
            prompt += String.format("Q%d: %s\nA: %s\nUser Answer: %s\n\n", i + 1, questions.get(i), answers.get(i), userAnswers.get(i));
        }

        prompt += "Return the results in the format <score, isCorrect> where score is an integer and isCorrect is a list of 20 boolean values.\n 다른 부연설명을 일체 하지 말고 정해진 형식만 출력해줘.";

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
        String[] parts = response.replace("<", "").replace(">", "").split(",");
        result.put("score", Integer.parseInt(parts[0].trim()));
        result.put("isCorrect", Arrays.stream(parts[1].trim().split(" ")).map(Boolean::parseBoolean).collect(Collectors.toList()));
        return result;
    }



}