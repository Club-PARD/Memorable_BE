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

    public static String buildTestSheetPrompt(List<String> keywords, String text) {
        String keywordList = String.join(", ", keywords);
        return String.format("### Situation ###\n" +
                    "You are a professor who created the provided text. Your role is to help students learn. You need to generate exam questions based on the text.\n" +
                    "### Instruction ###\n" +
                    "Based on the provided text, generate 20 high-quality questions and answers.\n" +
                    "The purpose is to verify the understanding of the text by learners. Ensure that the questions and answers are of a difficulty level suitable for university and graduate students.\n" +
                    "The questions should not be obvious and must require reading the text to answer correctly.\n" +
                    "### Format ###\n" +
                    "1. Question: Formulate a question that allows the answer to be inferred from the text. Each question should be unique and clearly lead to a single, specific answer without ambiguity.\n" + "Questions should be in Korean and 존댓말 형식이어야 한다(ex. ~무엇인가요?/~ 무엇입니까? etc.)\n" +
                    "2. Answer: Provide a precise answer that can be directly found within the text. Answers form should not exceed 20 characters in length. Also, there should be the reason why this answer is correct based on the text. You don't need to send the reason.\n" +
                    "### Guidelines ###\n" +
                    "1. Ensure the quality of questions and answers is excellent.\n" +
                    "2. Questions and answers should not include any additional explanations or comments not found in the text.\n" +
                    "3. If the final generated questions and answers are not exactly 20, regenerate them until there are exactly 20. Do not return the result until exactly 20 questions and answers are created.\n" +
                    "4. Generate them as quickly as possible.\n" +
                    "5. Questions must start with Q: and answers with A: without any line breaks in between.\n" +
                    "ex. Q: [Question}\nA: {Answer]\nQ: [Question}\nA: {Answer]"+
                    "**Final note: If the final generated questions and answers are not exactly 20, regenerate them until there are exactly 20. Do not return the result until exactly 20 questions and answers are created.**\n" +
                    "6. The text of the correct answer must never be included in the problem sentence.\n" +
                    "7. 특정 문제 내의 그 어떤 단어도 정답이 되어서는 안돼!! 예를 들어 문제 1번에 답이 \"사과\"라면 1번 문제의 문장에는 절대 \"사과\"가 들어가면 안돼!\n"+
                    "8. \"네/아니오\"와 같은 정답을 요구하는 문제는 금지한다.\n" +
                    "10. 너무 뻔한 문제와 답은 안돼!그리고 \"~한 이유는 무엇인가?\"와 같이 문장 형식의 답을 요구하는 문제는 안돼.\nㄱn" +
                    "11. 문제에서 원하는 정답이 사람, 사상, 주제 등 많은 객체 중 무엇인지 정확하게 명시해야 해. 예를 들어, \"~에 해당하는 사상은 무엇인가?\"라는 질문에 정답이 사람이거나 다른 물체이면 잘못된 추출이야. 예를 들어 사상을 물어봤으면 택스트 내에 언급된 특정 사상이 정답이어야돼.\n" +
                    "12. 제일 강조: 20개의 정답은 서로 절대 겹치면 안돼!!\n최종적으로 정답 중에 중복이 있으면 중복이 없을때까지 다시 추출해.\n" +
                    "**Ensure the quality of questions is excellent.**\n" +
                    "**Generate them as quickly as possible.**\n" +
                    "\n" +
                    "### Text ###\n" +
                    "%s\n",
             keywordList, text);
    }

    public static String scoreAnswersPrompt(String content, List<String> questions, List<String> answers, List<String> userAnswers) {
        StringBuilder prompt = new StringBuilder(String.format(
//                "Please grade the following set of 20 questions and answers. " +
                "Please grade the following set of 20 questions and answers. " +
                        "Read the content and understand its context. " +
                        "Compare each answer and user answer against the content and questions. " +
                        "If the user's answer is similar to the actual answer based on the content and questions, mark it as correct. " +
                        "만약 사용자의 정답이 한글이고 실제 정답이 영어인데 둘의 의미가 비슷하다면 정답으로 처리해.\n "+
                        "Change the corresponding index in 'isCorrect' to true.\n\n" +
                        "Content: %s\n\n" +
                        "Questions and Answers and user answers:\n\n", content
        ));

        for (int i = 0; i < questions.size(); i++) {
            log.info("Q: "+questions.get(i)+"\nA: "+ answers.get(i) + "\nUser Answer: " + userAnswers.get(i));
            prompt.append(String.format("Q%d: %s\nA%d: %s\nUser Answer: %s\n\n", i + 1, questions.get(i), i+1, answers.get(i), userAnswers.get(i)));
        }
        prompt.append("Return the results in the format <isCorrect> where isCorrect is a list of 20 boolean values.\n 다른 부연설명을 일체 하지 말고 정해진 형식만 출력해줘.");
        prompt.append("\"사용자의 정답이 빈칸이면 무조건 오답으로 처리해.\\n\"");
        prompt.append("그리고 각 채점에 대해 왜 정답 처리를 했는지, 왜 오답 처리를 했는지 간단한 설명을 첨부해줘.");
        return prompt.toString();
    }
}
