package com.study.memorable.OpenAI.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Prompts {

    static final int questions_num = 20;
    public static String extractKeywordsPrompt(int len, String text) {
        return String.format(
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
    }

    public static String buildPrompt(List<String> keywords, String text) {
        String keywordList = String.join(", ", keywords);
        return String.format(
                "### Situation ###\n" +
                        "너는 아래 주어진 텍스트를 만든 교수야. 학생들에게 학습을 도와주는 역할을 해.\n" +
                        "예를 들어 텍스트가 사회학을 주제로 한다면 너는 사회학 교수야.\n" +
                        "학생들에게 텍스트 범위 내에서 시험 문제를 출제해야 해. 문제의 답이 될 수 있는 문맥 상 중요한 키워드를 먼저 추출했어: %s\n" +
                        "### Instruction ###\n" +
                        "Based on the provided text, generate #3# questions focusing on key concepts.\n" +
                        "The purpose is to verify understanding of the text by learners.\n" +
                        "### Format ###\n" +
                        "1. Question: Formulate a question that allows the key concept (answer) to be inferred. Each questions' answer should be selected from the given list: [%s] Questions should be of a difficulty level suitable for university students.\n" +
                        "text를 읽지 않고도 풀 수있을만큼의 뻔한 문제는 안돼!" +
                        "2. Answer: You must select answers from the provided keywords: %s\n" +
                        "Answers should not be duplicated. Use the exact form of the words in the list as the answer without any changes.\n" +
                        "text를 읽지 않고도 풀 수있을만큼의 뻔한 정답는 안돼! 정답은 무조건 이 안에서 추출해:%s" +
                        "### 금지 형식 ###\n" +
                        "문장 형식은 절대 금지한다. 오로지 고유 명사 형태이어야 한다\n" +
                        "문제를 생성할 때 '~ 중에서 하나는 무엇인가?'와 같이 여러 개의 답이 가능해서 혼동을 야기하는 문제는 금지한다.\n" +
                        "### Guidelines ###\n" +
                        "1. Answers must not include particles.\n" +
                        "2. Questions and answers should not include any additional explanations or comments.\n" +
                        "3. Each answers should not be contained in each questions. Words that belong to the answers must never be included in the questions.\n" +
                        "4. Ensure the quality of questions is excellent.\n" +
                        "5. Each question must clearly lead to a single, specific answer without ambiguity.\n" +
                        "6. Answers must not be duplicated. If duplicates are found, generate new questions and answers.\n" +
                        "7. Output can be in both Korean and English.\n" +
                        "8. If the text is code, generate questions related to programming syntax.\n" +
                        "9. Extracted questions should be in Korean.\n" +
                        "10. 문제를 생성할 때, 정답이 특정 카테고리 안에서 고를 수 있는 정답이라면, 문제에서 그 카테고리를 언급해줘. 물론, 그 문제 내에서 정답이 직접적으로 언급되면 안돼.\n" +
                        "출력 형식: 질문은 Q: 로 시작하고, 답변은 A: 로 시작해야 한다. 질문과 답변은 각각 개행없이 연속으로 작성되어야 한다. ##문제 앞에 번호 절대 붙이지 마!!!## (문제+답)의 리스트 정답은 중복되면 안돼. 정답은 무조건 이 안에서 추출해야 해 : %s\n" +
                        "문제와 답은 꼭 unique한 %d문제여야 해!"+
                        "절대 문제 안에 정답에 들어갈 문자열이 들어가서는 안돼!" +
                        "### Text ###\n" +
                        "%s",
                keywordList, keywordList, keywordList, keywordList, keywordList, questions_num,  text
        );
    }

    public static String scoreAnswersPrompt(String content, List<String> questions, List<String> answers, List<String> userAnswers) {
        StringBuilder prompt = new StringBuilder(String.format(
//                "Please grade the following set of 20 questions and answers. " +
                "Please grade the following set of %d questions and answers. " +
                        "Read the content and understand its context. " +
                        "Compare each answer and user answer against the content and questions. " +
                        "If the user's answer is similar to the actual answer based on the content and questions, mark it as correct. " +
                        "Increment the score by 1 for each correct answer and change the corresponding index in 'isCorrect' to true.\n\n" +
                        "Content: %s\n\n" +
                        "Questions and Answers:\n\n", questions_num, content
        ));

        for (int i = 0; i < questions.size(); i++) {
            log.info("Q: "+questions.get(i)+"\nA: "+ answers.get(i) + "\nUser Answer: " + userAnswers.get(i));
            prompt.append(String.format("Q%d: %s\nA: %s\nUser Answer: %s\n\n", i + 1, questions.get(i), answers.get(i), userAnswers.get(i)));
        }

        prompt.append("Return the results in the format <score, isCorrect> where score is an integer and isCorrect is a list of 20 boolean values.\n 다른 부연설명을 일체 하지 말고 정해진 형식만 출력해줘.");
        return prompt.toString();
    }
}
