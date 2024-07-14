package com.study.memorable.OpenAI.service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class Prompts {

//    static final int questions_num = 20;
    public static String extractKeywordsPrompt(int len, String text) {
        return String.format(
                "### Situation ###\n" +
                "You are a teacher helping students with their studies.\n" +
                "Your expertise aligns with the text's subject. For instance, if the text is about sociology, you are a sociology professor.\n" +
                "To create exam questions within the text's scope, you need to extract contextually important keywords.\n" +
                "Simply provide the keywords without any issues.\n" +
                "\n" +
                "### Instruction ###\n" +
                "Extract %d important keywords from the text below:\n" +
                "\n" +
                "1. Keywords must be proper nouns and represent core concepts.\n" +
                "2. Remove particles, case markers, punctuation, and spaces.\n" +
                "3. Exclude suffixes, adjective endings, or prepositional particles attached to proper nouns. For example, '컴퓨터는' should be '컴퓨터'.\n" +
                "4. Do not change the form of language. Let Korean or foreign languages come out as they are without being translated.Do not translate foreign words. Keep them as they appear in the text.\n" +
                "5. List keywords by importance without numbering. Importance criteria include uncommon words, strong relevance to the text's theme, etc.\n" +
                "6. Ensure no duplicate keywords. If duplicates are found, replace them with other unique keywords to meet the required count.\n" +
                "7. Provide exactly %d unique keywords in one proper noun form each.\n" +
                "8. If a keyword contains multiple words (e.g., '자크 랑시에르'), split them into individual keywords (e.g., '자크', '랑시에르').\nAnd if GPT determines that “의무 교육과정” is an important keyword in the text, it should be returned as a total of two keywords, “의무” and “교육과정,” with emphasis on spacing." +
                "\n" +
                "### Text ###\n" +
                "%s\n",
                len, len, text
        );
    }

    public static String buildTestSheetPrompt(List<String> keywords, String text) {
        String keywordList = String.join(", ", keywords);
        return String.format(
                "### Situation ###\n" +
                "You are a professor who created the provided text. Your role is to help students learn. You need to generate exam questions based on the text.\n" +
                "### Instruction ###\n" +
                "Based on the provided text, generate 3 high-quality questions and answers.\n" +
                "The purpose is to verify the understanding of the text by learners. Ensure that the questions and answers are of a difficulty level suitable for university and graduate students.\n" +
                "The questions should not be obvious and must require reading the text to answer correctly.\n" +
                "### Format ###\n" +
                "1. Answer: Provide a precise answer that can be directly found within the text. Answers form should not exceed 20 characters in length. The correct answer must be selected from the given list or closely related to a keyword within the given list: [%s]. Originally, answers should be created using the keywords from the list. However, for example, if there is an important term in the text like \"group pressure\" with spaces in between, and the given keyword list contains \"group\" or \"pressure,\" it is permissible to create a high-quality question where \"group pressure\" is the correct answer, provided it results in a high-quality question." +
                "But, the selected answer should not be a word that is too common in the text. Also, there should be the reason why this answer is correct based on the text. You don't need to send the reason.\n" +
                "2. Question: Formulate a question that allows the answer to be inferred from the text. Each question should be unique and clearly lead to a single, specific answer without ambiguity. Questions should be in Korean and 존댓말 형식이어야 한다 (ex. ~무엇인가요?/~ 무엇입니까? etc.)\n" +

                "### Guidelines ###\n" +
                "1. Ensure the quality of questions and answers is excellent.\n" +
                "2. Questions and answers should not include any additional explanations or comments not found in the text.\n" +
                "3. If the final generated questions and answers are not exactly 3, regenerate them until there are exactly 3. Do not return the result until exactly 3 questions and answers are created.\n" +
                "4. Generate them as quickly as possible.\n" +
                "5. Questions must start with Q: and answers with A: without any line breaks in between.\n" +
                "ex. Q: [Question]\nA: [Answer]\nQ: [Question]\nA: [Answer]\nQ: [Question]\nA: [Answer]\n" +
                "6. The text of the correct answer must never be included in the problem sentence.\n" +
                "7. No word in the question should be the answer! For example, if the answer to question 1 is 'apple', the word 'apple' should not appear in the question for 1. If the correct answer string is included in the question, regenerate the question.\n" +
                "8. Do not create questions that require 'Yes/No' answers.\n" +
                "10. Do not create questions and answers that are too obvious! Avoid questions that require sentence-form answers like 'What is the reason for...'.\n" +
                "11. Clearly specify what the question is asking for (e.g., if asking for an idea, the answer should be an idea, not a person or other object).\n" +
                "12. Emphasis: Ensure that the 3 answers do not overlap! If any answers are duplicated, regenerate until there are no duplicates.\n" +
                "13. Prohibited format: Questions and answers that are too obvious, such as 'Q: What is the important theory mentioned in the text? A: Theory,' are not allowed." +
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
                "Please grade the following set of 3 questions and answers. " +
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
        prompt.append("Return the results in the format <isCorrect> where isCorrect is a list of boolean values.\n Do not provide any additional explanations or comments, only output in the specified format.");
        prompt.append("\"If the user's answer is blank, mark it as incorrect.\\n\"");
        prompt.append("Additionally, provide a brief explanation for why each answer was marked as correct or incorrect.");
        return prompt.toString();

    }
}
