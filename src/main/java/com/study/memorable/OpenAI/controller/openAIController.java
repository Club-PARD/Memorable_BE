package com.study.memorable.OpenAI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.study.memorable.OpenAI.dto.ChatGPTRequest;
import com.study.memorable.OpenAI.dto.ChatGPTResponse;
import com.study.memorable.OpenAI.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api")
public class openAIController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenAIService openAIService;

    String text = "먼저 무지한 스승의 저자인 자크 랑시에르의 철학적 배경의 시작부터 소개하고자 한다. 랑시에르는 프랑스의 마르크스주의 철학자인 알튀세르의 제자로서 구조주의와 포스트구조주의 철학에 큰 영향을 받은 철학자였다. 그렇기에 랑시에르는 철학은 프랑스의 사회구조의 영향과 알튀세르의 영향을 받아 권력, 평등, 해방에 초점을 맞추어 전개된다. 이는 책 기저에 깔려 있는 평등한 지적 능력을 비롯한 평등한 교육의 권리 등에 대한 철학적 배경을 제공한다. \n" +
            "그리고 조제프 자코토의 교육 실험과 역사적인 맥락을 짚고 넘어가고자 한다. 조세프 자코토는 프랑스의 교육자였으며, 교육을 전담했던 시기는 프랑스 혁명 이후였다. 이 시기는 사회의 모든 구조가 변혁을 맞이하던 당시였으며, 새로운 교육방법이 제시되어 기반 자체에 대한 새로운 도전들이 늘어나던 시기였다. 자코토의 교육 실험은 이러한 시기 속에서 벨기에에서 프랑스어를 가르치는 수업을 진행하면서 발생했는데, 네덜란드어를 전혀 모르는 자코토가 프랑스어를 전혀 모르는 학생들에게 가르침을 주는 경험 속에서 시작됐다.\n" +
            "위 교육 실험이 주장하는 주요 개념에 대해서 랑시에르는 모든 인간이 본질적으로 동일한 지적 능력을 가지고 있다. 라고 주장한다. 이에 대해서 비판적으로 바라본 부분은 바로 교육시스템에 있다. 어떠한 특정 교육 시스템이 학생들로 하여금 무지를 교육하며, 지적능력에 제한을 가하고 있다는 것이다. 이를 전통적인 교육 시스템이라고 특정하였고, 이에 대한 새로운 교육 시스템의 등장 및 교육 시스템의 변혁을 중심으로 논지를 전개한다.\n" +
            "모국어의 경우 아이들에게 가르치지 않아도 어느 순간 이들의 일상속에 녹아 배움이 일어난다. 그리고 평등한 지적능력의 영역에서 누군가는 기호와 말을 만들기도 하고, 누군가는 기계를 만드는 지능까지의 분화가 일어나는 것이다. 이것은 가르치는 사람에 대한 부분의 영역이 아니다 그저 해방까지의 길을 제시하고 끌어내 줄 수 있는 사람의 존재가 중요한 것이다. 그리고 우리는 이를 무지한 스승이라고 일컫게 되는것이다.\n";


    @PostMapping("/extract-keywords")
    public ObjectNode extractKeywordsFromContent(@RequestBody String text) {
        log.info("model: " + model);

        int textLengthWithoutSpaces = text.replace(" ", "").replace("\n", "").length();
        int len = (int) (textLengthWithoutSpaces * 0.03);

        log.info("text len: " + textLengthWithoutSpaces);
        log.info("len: " + len);

        List<String> keywords = extractKeywordsFromText(len, text);
        log.info("Extracted Keywords: " + keywords);

        Map<String, Object> keywordResults = processKeywords(keywords, text);
        String replacedText = (String) keywordResults.get("replacedText");
        List<String> arr = (List<String>) keywordResults.get("arr");

        return createResponseJson(text, keywords, replacedText, arr);
    }

    private List<String> extractKeywordsFromText(int len, String text) {
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
        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        assert response != null;
        return response.getChoices().stream()
                .flatMap(choice -> extractKeywordsFromMessage(choice.getMessage().getContent()).stream())
                .distinct()
                .limit(1000)
                .collect(Collectors.toList());
    }

    private Map<String, Object> processKeywords(List<String> keywords, String text) {
        StringBuilder replacedText = new StringBuilder();
        List<String> arr = new ArrayList<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            boolean isKeyword = false;
            for (String key : keywords) {
                if (word.contains(key)) {
                    replacedText.append(word.replace(key, "_____")).append(" ");
                    arr.add(key);
                    isKeyword = true;
                    break;
                }
            }
            if (!isKeyword) {
                replacedText.append(word).append(" ");
            }
        }

        return Map.of("replacedText", replacedText.toString(), "arr", arr);
    }

    private ObjectNode createResponseJson(String text, List<String> keywords, String replacedText, List<String> arr) {
        String formattedKeywords = formatKeywordsAsList(keywords);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonResponse = mapper.createObjectNode();
        jsonResponse.put("Content", text);
        jsonResponse.put("formattedKeywords", formattedKeywords);
        jsonResponse.put("replacedText", replacedText);
        jsonResponse.putPOJO("Answer", arr);

        log.info("\nKeywords: " + formattedKeywords);
        log.info("\nreplacedText: " + replacedText);
        log.info("\nAnswer: " + arr);

        return jsonResponse;
    }

    @PostMapping("/grade-answers")
    public ObjectNode gradeAnswers(@RequestBody Map<String, List<String>> requestBody) {
        List<String> generatedQuestions = requestBody.get("questions");
        List<String> correctAnswers = requestBody.get("correctAnswers");
        List<String> studentAnswers = requestBody.get("studentAnswers");

        String promptGradeAnswers = String.format(
                """
                ### Situation ###
                너는 학생들의 시험 답안을 채점하는 선생님이야.
                주어진 정답 리스트와 학생의 답안을 비교하여 채점해야 해.
                정답과 학생의 답안이 조사의 유무 차이, 문장 구조의 차이 등의 차이가 있더라도 의미적으로 동일하다면 정답으로 처리해야 해.
                ### Instruction ###
                Compare the provided correct answers with the student's answers. If the student's answer is semantically the same as the correct answer, mark it as correct.
                ### Example ###
                Correct Answer: "전통적인 방식"
                Student Answer: "전통적 방식"
                Result: Correct
                Correct Answer: "프랑스 혁명"
                Student Answer: "프랑스 대혁명"
                Result: Correct
                ### Format ###
                1. Provide a list of results indicating whether each student's answer is correct or incorrect.
                2. Use the following format:
                   Q1: Correct
                   Q2: Incorrect
                   ...
                   Q#: [정답의 질문]
                   Student Answer: [학생의 답변]
                   Correct Answer: [정답]
                   Result: [Correct or Incorrect]
                ### Data ###
                Origin text: %s
                Generated Questions: %s
                Correct Answers: %s
                Student Answers: %s
                """,
                text, String.join("\n", generatedQuestions), String.join("\n", correctAnswers), String.join(", ", studentAnswers)
        );

        ChatGPTRequest gradeRequest = new ChatGPTRequest(model, promptGradeAnswers);
        ChatGPTResponse gradeResponse = restTemplate.postForObject(apiURL, gradeRequest, ChatGPTResponse.class);

        ObjectNode jsonResponse = new ObjectMapper().createObjectNode();

        if (gradeResponse != null && gradeResponse.getChoices() != null && !gradeResponse.getChoices().isEmpty()) {
            String gradeContent = gradeResponse.getChoices().get(0).getMessage().getContent();
            log.info("\nGrading Result:\n" + gradeContent);
            jsonResponse.put("gradingResult", gradeContent);
        } else {
            log.info("GPT API한테서 받아온 데이터 없음.");
            jsonResponse.put("gradingResult", "No response from GPT API");
        }

        return jsonResponse;
    }

    private List<String> extractKeywordsFromMessage(String content) {
        return Arrays.stream(content.split(",\\s*"))
                .flatMap(keyword -> Arrays.stream(keyword.split("\\s+")))
                .collect(Collectors.toList());
    }

    private String formatKeywordsAsList(List<String> keywords) {
        return String.join("\n", keywords);
    }

    @PostMapping("/getTokenCount")
    public int getTokenCount(@RequestBody Map<String, String> requestBody) {
        String text = requestBody.get("text");
        try {
            return openAIService.getTokenCount(text);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
