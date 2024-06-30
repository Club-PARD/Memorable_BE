package com.pard.Memorable.demo.controller;

import com.pard.Memorable.demo.dto.ChatGPTRequest;
import com.pard.Memorable.demo.dto.ChatGPTResponse;
import com.pard.Memorable.demo.service.OpenAIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/api")
public class WorkSheetController {

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

    String text = "-   대응원칙이란 관련없는 구조의 연관성과 대응관계가 존재한다는 것에 대한 것을 말한다. 이는 학교와 노동의 생활이 밀접하게 연관되어 있는데, 예를 들면 학교에서의 규율, 권위에 대한 복종, 시간 엄수, 경쟁 등은 직장에서 비슷하게 나와있다는 것이다. 구조적 유사성은 학생들이 학교에서 그 이후의 사회화에서도 큰 어려움 없이 녹아들 수 있음을 입증하는 것과도 같다. \n" + "-   숨겨진 교육과정은 공식적인 과정 외에도 학교가 학생들에게 은연중에 드러내는 상호작용에 대해서 학생들에게 가르치는 것이 있다는 것이다. 예를 들어 학교에서의 규율과 경쟁들은 사회에 나가서도 비슷하게 따르도록 만드는 것이다. 이러한 숨겨진 교육과정은 위에서 나온 대응원칙과도 연관성을 띄고 사람들의 내면에 숨어있는 가치관에도 영향을 주어 성장하게 해 준다는 것이다. \n" + "-   같은 노동계급의 1세대의 자녀들이 2세대에서도 노동계급을 선택하는 것은 그들의 자아 형성과 관련이 있다고 말한다. 이는 학문적 성공의 가치보다는 실질적인 기술과 노동을 중시하는 가르침에서 발생하는데 이는 부모 세대의 직업을 이어받는 것 자체에 대한 자부심과 공동체 의식에서 발현되는 것이다. 이로 인해 이들은 자발적 복종이라는 내용으로 정의하며 이들의 이러한 선택들을 사회적, 문화적 맥락에서 바라보는 것이 옳다고 말한다. \n" + "\n" + "-   일반화된 타자는 쉽게 말해 사회전반에 걸쳐 만들어진 개인의 기대감이다. 이는 개인에게 할당된 기대감이 있는 것이고 이 기대감은 여러 기준점을 통해서 정의된다는 것이다. 이 기준점으로는 사회가치, 규범과 태도 등이 있고 이를 통해 사회의 기준을 내면화 시키는 것이다. 그리고 이 내면화된 사회의 기대감은 개인의 자아형성과 사회안에서의 개인의 정체성 발달에 영향을 끼친다. 사회라는 집단 속에서 개인의 일반화가 이루어지는 개념인 것이다.\n" + "-   미드는 I와 ME의 경계선을 만들어 짓는다. 먼저 I는 개인의 자아가 즉흥적이고 충동적으로 행동하는 모든 것을 설명한다. 어떠한 행동을 할 때 그 상황 속에서 반응하는 부분을 설명한다. 이제 양립하는 정체성으로 ME는 일반화된 타자와 같이 사회의 기대, 규범, 태도를 내면화 하여 사회의 틀 안에서 일반화하는 개념을 설명한다. 사회라는 틀 안에서 자아를 성찰하며 사회화의 과정을 거친다는 것이다. \n" + "-   마르크스는 역사적 물질주의에 대해서 이야기한다. 인간 개인들의 사회적 의식, 신념, 가치와 같은 개인이 만들어 간다고 생각했던 부분들이 더 넓은 문화적 상부구조를 형성한다는 것이다. 마르크스는 경제 생산 양식, 즉 특정 생산 수단의 소유와 노동력에 대한 관계를 말하며 양식별로 사회를 구성하는 것이 다르다고 말한다. \n";


    @PostMapping("/extract-keywords")
    public String extractKeywords() {
        log.info("model: " + model);

        int len = (int) (text.length() * 0.03); // 필요한 키워드의 2배를 추출 -> 홀수/짝수로 나누어서 키워드 2번 사용 가능
//        int len = 20;
        log.info("text len: " + text.length());
        log.info("len: " + len);

        String prompt = String.format(
                "### Instruction ###\n" +
                        "Extract %d important keywords from the text below:\n" +
                        "1. Keywords must be proper nouns and represent core concepts.\n" +
                        "2. Remove particles, case markers, and punctuation.\n" +
                        "3. Exclude suffixes or particles attached to proper nouns. For example, '컴퓨터는' should be '컴퓨터'.\n" +
                        "4. Do not translate foreign words. Keep them as they appear in the text.\n" +
                        "5. List keywords by importance without numbering. Importance critercore to understanding the tia: uncommon words, strongly related to the text's theme, and ext.\n" +
                        "6. Ensure no duplicate keywords. If duplicates are found, replace them with other unique keywords to meet the required count.\n" +
                        "7. Provide exactly %d unique keywords in one proper noun form each.\n" +
//                        "8. If a keyword contains foreign words written in Korean, output only the foreign word without any Korean particles.\n" +
                        "9. Output can be in both Korean and English.\n" +
                        "### Text ###\n" +
                        "%s",
                len, len, text
        );



        ChatGPTRequest request = new ChatGPTRequest(model, prompt);

        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        List<String> keywords = response.getChoices().stream()
                .flatMap(choice -> extractKeywordsFromMessage(choice.getMessage().getContent()).stream())
                .distinct() // 중복된 키워드 제거
                .limit(1000) // 키워드 개수 제한
                .collect(Collectors.toList());


        String keyword = response.getChoices().get(0).getMessage().getContent();

        log.info("\nKeyword: " + keyword);
//
//        String formattedKeywords = formatKeywordsAsList(keywords);
//
//        openAIService.setKeywords(keywords);
//
//        StringBuilder replacedText = new StringBuilder();
//        List<String> arr = new ArrayList<>();
//
//        String[] words = text.split("\\s+");
//
//        for (String word : words) {
//            boolean isKeyword = false;
//            for (String key : keywords) {
//                if (word.contains(key)) {
//                    replacedText.append(word.replace(key, "_____")).append(" ");
//                    arr.add(key);
//                    isKeyword = true;
//                    break;
//                }
//            }
//            if (!isKeyword) {
//                replacedText.append(word).append(" ");
//            }
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode jsonResponse = mapper.createObjectNode();
////        jsonResponse.put("Content", text);
//        jsonResponse.put("formattedKeywords", formattedKeywords);
////        jsonResponse.put("replacedText", replacedText.toString());
////        jsonResponse.putPOJO("Answer", arr);
//
//
//        // ###########################################################################
//        String prompt1 = String.format(
//                "### Instruction ###\n" +
//                        "Based on the provided text, generate 20 questions and answers focusing on key concepts.\n" +
//                        "The purpose is to verify understanding of the text by learners.\n" +
//                        "### Format ###\n" +
//                        "1. Question: Formulate a question that allows the key concept (answer) to be inferred. Questions should be of a difficulty level suitable for university students.\n" +
//                        "문제의 난이도: 대학원생, 교수에 맞게" +
//                        "2. Answer: You must select answers from the provided keywords: %s\n" +
//                        "Answers should not be duplicated."+
//                        "Use the exact form of the words in the list as the answer without any changes.\n" +
//                        "### Example ###\n" +
//                        "Text: '기독교가 역사에서 살아남음=기독교가 상황에 잘 적응하느냐?'\n" +
//                        "Q: 기독교가 역사에서 살아남은 것은 무엇과 관련이 있는가?\n" +
//                        "A: 상황 적응\n" +
//                        "Text: (컴퓨터가 사람에게 도움을 주는 텍스트의 일부 내용이라고 가정)"+
//                        "Q: 사람에게 도움을 준 도구로 언급된 것은?\n" +
//                        "A: 컴퓨터\n" +
//                        "###금지 형식###" +
//                        "문제를 생성할 때 '~ 중에서 하나는 무엇인가?'왁 같이 여러 개의 답이 가능하면서 하나의 답만을 정답으로 인정하는 문제는 금지한다."+
//                        "### Guidelines ###\n" +
//                        "1. Answers must not include particles.\n" +
//                        "2. Questions and answers should not include any additional explanations or comments.\n" +
//                        "3. Questions must not contain the answer. Words that belong to the answers must never be included in the questions.\n" +
//                        "4. Ensure the quality of questions is excellent.\n" +
//                        "5. Each question must clearly lead to a single, specific answer without ambiguity.\n" +
//                        "6. Answers must not be duplicated. If duplicates are found, generate new questions and answers.\n" +
//                        "7. Output can be in both Korean and English.\n" +
//                        "8. If the text is code, generate questions related to programming syntax.\n" +
//                        "9. Extracted questions should be in Korean.\n" +
//                        "### Text ###\n" +
//                        "%s",
//                String.join(", ", keywords), text
//        );
//
//
//
//        ChatGPTRequest request1 = new ChatGPTRequest(model, prompt1);
//
//        ChatGPTResponse response1 = restTemplate.postForObject(apiURL, request1, ChatGPTResponse.class);
//
//        if (response1 != null && response1.getChoices() != null && !response1.getChoices().isEmpty()) {
//            String content = response1.getChoices().get(0).getMessage().getContent();
//            log.info("Content:\n" + content);
//        } else {
//            log.info("GPT API한테서 받아온 데이터 없음.");
//        }

        return "good!!";
    }

    private List<String> extractKeywordsFromMessage(String content) {
        return Arrays.asList(content.split(",\\s*"));
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

