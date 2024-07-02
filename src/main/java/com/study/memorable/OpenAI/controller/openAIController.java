package com.study.memorable.OpenAI.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class openAIController {

//    @Value("${openai.model}")
//    private String model;
//
//    @Value("${openai.api.url}")
//    private String apiURL;
//
//    @Value("${openai.api.key}")
//    private String apiKey;

//    @Autowired
//    private RestTemplate restTemplate;

//    @Autowired
//    private OpenAIService openAIService;

//    String text = "먼저 무지한 스승의 저자인 자크 랑시에르의 철학적 배경의 시작부터 소개하고자 한다. 랑시에르는 프랑스의 마르크스주의 철학자인 알튀세르의 제자로서 구조주의와 포스트구조적의 철학에 큰 영향을 받은 철학자였다. 그렇기에 랑시에르의 철학은 프랑스의 사회구조의 영향과 알튀세르의 영향을 받아 권력, 평등, 해방에 초점을 맞추어 전개된다. 이는 책 기저에 깔려 있는 평등한 지적 능력을 비롯한 평등한 교육의 권리 등에 대한 철학적 배경을 제공한다. \n" + "그리고 조제프 자코토의 교육 실험과 역사적인 맥락을 짚고 넘어가고자 한다. 조세프 자코토는 프랑스의 교육자였으며, 교육을 전담했던 시기는 프랑스 혁명 이후였다. 이 시기는 사회의 모든 구조가 변혁을 맞이하던 당시였으며, 새로운 교육방법이 제시되어 기반 자체에 대한 새로운 도전들이 늘어나던 시기였다. 자코토의 교육 실험은 이러한 시기 속에서 벨기에에서 프랑스어를 가르치는 수업을 진행하면서 발생했는데, 네덜란드어를 전혀 모르는 자코토가 프랑스어를 전혀 모르는 학생들에게 가르침을 주는 경험 속에서 시작됐다.\n" + "위 교육 실험이 주장하는 주요 개념에 대해서 랑시에르는 모든 인간이 본질적으로 동일한 지적 능력을 가지고 있다. 라고 주장한다. 이에 대해서 비판적으로 바라본 부분은 바로 교육시스템에 있다. 어떠한 특정 교육 시스템이 학생들로 하여금 무지를 교육하며, 지적능력에 제한을 가하고 있다는 것이다. 이를 전통적인 교육 시스템이라고 특정하였고, 이에 대한 새로운 교육 시스템의 등장 및 교육 시스템의 변혁을 중심으로 논지를 전개한다.\n" + "모국어의 경우 아이들에게 가르치지 않아도 어느 순간 이들의 일상속에 녹아 배움이 일어난다. 그리고 평등한 지적능력의 영역에서 누군가는 기호와 말을 만들기도 하고, 누군가는 기계를 만드는 지능까지의 분화가 일어나는 것이다. 이것은 가르치는 사람에 대한 부분의 영역이 아니다 그저 해방까지의 길을 제시하고 끌어내 줄 수 있는 사람의 존재가 중요한 것이다. 그리고 우리는 이를 무지한 스승이라고 일컫게 되는것이다.\n";

    @PostMapping("/keyword")
    public String extractKeywords(){
        return "good";
    }

}


