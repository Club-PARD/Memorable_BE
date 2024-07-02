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

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Value("${openai.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("")
    public String createFile(@RequestBody FileCreateDTO dto){
        fileService.createFile(dto);
        return "파일 생성됨!";
    }

    @GetMapping("")
    public List<FileReadDTO> findAll(){
        return fileService.findAll();
    }

    String text = "먼저 무지한 스승의 저자인 자크 랑시에르의 철학적 배경의 시작부터 소개하고자 한다. 랑시에르는 프랑스의 마르크스주의 철학자인 알튀세르의 제자로서 구조주의와 포스트구조적의 철학에 큰 영향을 받은 철학자였다. 그렇기에 랑시에르의 철학은 프랑스의 사회구조의 영향과 알튀세르의 영향을 받아 권력, 평등, 해방에 초점을 맞추어 전개된다. 이는 책 기저에 깔려 있는 평등한 지적 능력을 비롯한 평등한 교육의 권리 등에 대한 철학적 배경을 제공한다. \n" +
            "두 번째는 조제프 자코토의 교육 실험과 역사적인 맥락을 짚고 넘어가고자 한다. 조세프 자코토는 프랑스의 교육자였으며, 교육을 전담했던 시기는 프랑스 혁명 이후였다. 이 시기는 사회의 모든 구조가 변혁을 맞이하던 당시였으며, 새로운 교육방법이 제시되어 기반 자체에 대한 새로운 도전들이 늘어나던 시기였다. 자코토의 교육실험은 이러한 시기 속에서 프랑스어를 가르치는 수업을 진행하면서 발생했는데, 네덜란드어를 전혀 모르는 자코토가 프랑스어를 전혀 모르는 학생들에게 가르침을 주는 경험 속에서 시작됐다.\n" +
            "위 교육 실험이 주장하는 주요 개념에 대해서 랑시에르는 ‘모든 인간이 본질적으로 동일한 지적 능력을 가지고 있다.’라고 주장한다. 이에 대해서 비판적으로 바라본 부분은 바로 교육시스템에 있다. 어떠한 특정 교육 시스템이 학생들로 하여금 무지를 교육하며, 지적능력에 제한을 가하고 있다는 것이다. 이를 전통적인 교육 시스템이라고 특정하였고, 이에 대한 새로운 교육 시스템의 등장 및 교육 시스템의 변혁을 중심으로 논지를 전개한다.\n" +
            "모국어의 경우 아이들에게 가르치지 않아도 어느 순간 이들의 일상속에 녹아 배움이 일어난다. 그리고 평등한 지적능력의 영역에서 누군가는 기호와 말을 만들기도 하고, 누군가는 기계를 만드는 지능까지의 분화가 " +
            "일어나는 것이다. 이것은 가르치는 사람에 대한 부분의 영역이 아니다 그저 해방까지의 길을 제시하고 끌어내 줄 수 있는 사람의 존재가 중요한 것이다. 그리고 우리는 이를 무지한 스승이라고 일컫게 되는것이다. \n";
    // 🔥 프롬프트 시작

    @PostMapping("/extract-keywords")
    public String extractKeywords() {
        log.info("model: " + model);

        int len = (int) (text.length() * 0.03); // 필요한 키워드의 2배를 추출 -> 홀수/짝수로 나누어서 키워드 2번 사용 가능
//        int len = 20;
        log.info("text len: " + text.length());
        log.info("len: " + len);

        String prompt_keywords = String.format(
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



        ChatGPTRequest request = new ChatGPTRequest(model, prompt_keywords);

        ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);

        List<String> keywords = response.getChoices().stream()
                .flatMap(choice -> extractKeywordsFromMessage(choice.getMessage().getContent()).stream())
                .distinct() // 중복된 키워드 제거
                .limit(1000) // 키워드 개수 제한
                .collect(Collectors.toList());

//        openAIService.setKeywords(keywords);

        log.info("Response format: " + response);

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

        String formattedKeywords = formatKeywordsAsList(keywords);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonResponse = mapper.createObjectNode();
//        jsonResponse.put("Content", text);
        jsonResponse.put("formattedKeywords", formattedKeywords);
        jsonResponse.put("replacedText", replacedText.toString());
        jsonResponse.putPOJO("Answer", arr);

        log.info("\nKeywords: " + keywords);
        log.info("\nreplacedText: " +replacedText);
        log.info("\nAnswer; " + arr);

        // ###########################################################################
        String prompt_test = String.format(
                "### Instruction ###\n"
                        + "Based on the provided text, generate 20 questions focusing on key concepts.\n" +
                        "The purpose is to verify understanding of the text by learners.\n" +
                        "### Format ###\n"
                        + "1. Question: Formulate a question that allows the key concept (answer) to be inferred. Each questions' answer should be selected from the given list: [%s]"
                        + "Questions should be of a difficulty level suitable for university students.\n" +
                        "문제의 난이도: 대학원생, 교수에 맞게"
                        + "2. Answer: You must select answers from the provided keywords: %s\n" +
                        "Answers should not be duplicated."
                        + "Use the exact form of the words in the list as the answer without any changes.\n"
                        + "### Example ###\n"
                        + "Text: '기독교가 역사에서 살아남음=기독교가 상황에 잘 적응하느냐?'\n" +
                        "Q: 기독교가 역사에서 살아남은 것은 무엇과 관련이 있는가?\n" +
                        "A: 상황 적응\n" +
                        "Text: (컴퓨터가 사람에게 도움을 주는 텍스트의 일부 내용이라고 가정)"+
                        "Q: 사람에게 도움을 준 도구로 언급된 것은?\n" +
                        "A: 컴퓨터\n" +
                        "###금지 형식###" +
                        "문제를 생성할 때 '~ 중에서 하나는 무엇인가?'왁 같이 여러 개의 답이 가능하면서 하나의 답만을 정답으로 인정하는 문제는 금지한다."+
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
                        "10. 문제를 생성할 때, 정답이 특정 카테고리 안에서 고를 수 있는 정답이라면, 문제에서 그 카테고리를 언급해줘. 물론, 그 문제 내에서 정답이 직접적으로 언급되면 안돼." +
                        "출력 형식: (문제+답)의 리스트"+
                        "### Text ###\n" +
                        "%s",
                keywords, String.join(", ", keywords), text
        );



        ChatGPTRequest request1 = new ChatGPTRequest(model, prompt_test);

        ChatGPTResponse response1 = restTemplate.postForObject(apiURL, request1, ChatGPTResponse.class);

        if (response1 != null && response1.getChoices() != null && !response1.getChoices().isEmpty()) {
            String content = response1.getChoices().get(0).getMessage().getContent();
            log.info("\nContent:\n" + content);
        } else {
            log.info("GPT API한테서 받아온 데이터 없음.");
        }

        return "good!";
    }

    private List<String> extractKeywordsFromMessage(String content) {
        return Arrays.asList(content.split(",\\s*"));
    }

    private String formatKeywordsAsList(List<String> keywords) {
        return String.join("\n", keywords);
    }

//    @PostMapping("/getTokenCount")
//    public int getTokenCount(@RequestBody Map<String, String> requestBody) {
//        String text = requestBody.get("text");
//        try {
//            return openAIService.getTokenCount(text);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1;
//        }
//    }
}

