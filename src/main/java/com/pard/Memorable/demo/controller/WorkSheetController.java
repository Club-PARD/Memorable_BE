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

//    String text = "-   대응원칙이란 관련없는 구조의 연관성과 대응관계가 존재한다는 것에 대한 것을 말한다. 이는 학교와 노동의 생활이 밀접하게 연관되어 있는데, 예를 들면 학교에서의 규율, 권위에 대한 복종, 시간 엄수, 경쟁 등은 직장에서 비슷하게 나와있다는 것이다. 구조적 유사성은 학생들이 학교에서 그 이후의 사회화에서도 큰 어려움 없이 녹아들 수 있음을 입증하는 것과도 같다. \n" + "-   숨겨진 교육과정은 공식적인 과정 외에도 학교가 학생들에게 은연중에 드러내는 상호작용에 대해서 학생들에게 가르치는 것이 있다는 것이다. 예를 들어 학교에서의 규율과 경쟁들은 사회에 나가서도 비슷하게 따르도록 만드는 것이다. 이러한 숨겨진 교육과정은 위에서 나온 대응원칙과도 연관성을 띄고 사람들의 내면에 숨어있는 가치관에도 영향을 주어 성장하게 해 준다는 것이다. \n" + "-   같은 노동계급의 1세대의 자녀들이 2세대에서도 노동계급을 선택하는 것은 그들의 자아 형성과 관련이 있다고 말한다. 이는 학문적 성공의 가치보다는 실질적인 기술과 노동을 중시하는 가르침에서 발생하는데 이는 부모 세대의 직업을 이어받는 것 자체에 대한 자부심과 공동체 의식에서 발현되는 것이다. 이로 인해 이들은 자발적 복종이라는 내용으로 정의하며 이들의 이러한 선택들을 사회적, 문화적 맥락에서 바라보는 것이 옳다고 말한다. \n" + "\n" + "-   일반화된 타자는 쉽게 말해 사회전반에 걸쳐 만들어진 개인의 기대감이다. 이는 개인에게 할당된 기대감이 있는 것이고 이 기대감은 여러 기준점을 통해서 정의된다는 것이다. 이 기준점으로는 사회가치, 규범과 태도 등이 있고 이를 통해 사회의 기준을 내면화 시키는 것이다. 그리고 이 내면화된 사회의 기대감은 개인의 자아형성과 사회안에서의 개인의 정체성 발달에 영향을 끼친다. 사회라는 집단 속에서 개인의 일반화가 이루어지는 개념인 것이다.\n" + "-   미드는 I와 ME의 경계선을 만들어 짓는다. 먼저 I는 개인의 자아가 즉흥적이고 충동적으로 행동하는 모든 것을 설명한다. 어떠한 행동을 할 때 그 상황 속에서 반응하는 부분을 설명한다. 이제 양립하는 정체성으로 ME는 일반화된 타자와 같이 사회의 기대, 규범, 태도를 내면화 하여 사회의 틀 안에서 일반화하는 개념을 설명한다. 사회라는 틀 안에서 자아를 성찰하며 사회화의 과정을 거친다는 것이다. \n" + "-   마르크스는 역사적 물질주의에 대해서 이야기한다. 인간 개인들의 사회적 의식, 신념, 가치와 같은 개인이 만들어 간다고 생각했던 부분들이 더 넓은 문화적 상부구조를 형성한다는 것이다. 마르크스는 경제 생산 양식, 즉 특정 생산 수단의 소유와 노동력에 대한 관계를 말하며 양식별로 사회를 구성하는 것이 다르다고 말한다. \n";

    String text = "기독교 : 사유화된 가치들로 이루어 진 것 \n" +
            "상층부와 내면으로 나누어져 가치들을 내포하고 있는 모습이 있다\n" +
            "그리고 그것들은 개인의 영역에 가두어 놓았다.\n" +
            "이는 진리의 개념을 2가지 층으로 나누어서 개념을 이어나갔다\n" +
            "위층은 과학적으로 증명할 수 없는 것들이 주로 이루었고 아래층은 과학적으로 증명할 수 있는 것들이 주를 이루었다.\n" +
            "이상학과 형이상학의 조화로 진리를 탐구하는 사람들의 논지는 이어나갔다.\n" +
            "공산주의 : 복음을 정치적으로 봉쇄\n" +
            "공산주의는 전체적인 사상의 강압을 주고 있다. \n" +
            "공산주의 체제의 독재자의 신념과 이념에 주목하여야 하기에 종교의 자유성을 배재하고자 했다. \n" +
            "자유민주주의 : 특이점 x\n" +
            "자유민주주의는 이념 자체의 특성으로 시민들에게 이념의 자유를 주었다. \n" +
            "포스트모더니즘= 복음을 문화적으로 봉쇄\n" +
            "\uF0E8   풍선이 하늘로 올라가면 터지듯이 이념의 보따리로 종교를 무너지게 만들었다.\n" +
            "\uF0E8   누구나 진리에 다가가 종교를 만들고 자신을 믿는 상황을 만들었다. \n" +
            "\n" +
            "크리스토퍼 러브 : 배아줄기 세포 연구 존쟁에서의 발연: “공공정책의 문제를 논의할 때는 어떤 종교에게도 자리를 허락해서는 안된다.”\n" +
            "사탄의 목적 : 포스트모더니즘화 도구 사용\n" +
            "-   성경적 관점이 공적 영역에서 합법성을 가지지 못하도록 공격하는 가장 강력한 무기로 사용하는 상황이 도래했다.\n" +
            "느낀점- 종교와 정치 이념간의 관계에 대해서 생각할 수 있게 되었고, 이는 생각보다 밀접한 관계를 가지고 이미 우리들의 삶에 침투해 있다는 것을 깨달을 수 있는 수업이었다. 그렇기에 우리들은 더 정신을 바로 잡고 옳바를 가치관으로 세상을 바라봐야 할 것이다.\n" +
            "2021/11/18 12주차-2 기포모 강의 정리\n" +
            "\n" +
            "포스트 모더니즘이란 무엇인가?\n" +
            "현대-화살표- 체계화됨\n" +
            "포스트 모더니즘- 뒤엉킨 그물 같은 것- 아무 의미 없는 쓰레기\n" +
            "모더니즘의 중심- 합리성과 이성중심의 객관적인 진리관을 인정\n" +
            "모더니즘의 특성- 종교나 외적인 힘보다 인간의 이성에 대한 믿음을 강조했던 합리주의 시대\n" +
            "인간이 만들어낸 이야기 or 합리적인 이야기\n" +
            "합리성과 합리주의\n" +
            "1차, 2차 세계대전 합리주의를 따라가려다가-종교와 신념 등등을 모두 옮김\n" +
            "모더니즘= 지나친 합리주의 사고와 객관성의 주장\n" +
            "20세기에 도전 받기 시작\n" +
            "니체, 하이데거의 실존주의: 합리주의 한계도전\n" +
            "포스트모던 대표되는 학자들: 데리다, 푸고, 라캉, 리오타르 등등\n" +
            "세상을 살아가는 사람들의 사상을 변화시키고 옳바르지 못한 사상을 주입시킨다\n" +
            "알렉산더 칭기즈칸 나폴레옹\n" +
            "-   알렉산더가 가장 위대한 이유\n" +
            "-   알렉산더는 도서관을 짓고 철학자를 인정하고 그리스의 문화를 깔아놓음\n" +
            "로마가 그리스를 전부 먹었는데도 결국 그리스의 문화가 살아남음\n" +
            "칼보다 펜이 강하다(공산주의가 가장 싫어하는 것- 기독교 종교인, 사상가, 문화가)\n" +
            "그래서 예로부터 이들처럼 문화를 만들어가는 사람들을 다 죽이고 숙청했다. \n" +
            "문화의 강함, 그리고 그 문화가 종교를 잠식시키고 믿음의 성장을 막아섰다. \n" +
            "느낀점: 문화가 사람들을 움직이고 가치관을 잠식시킬 수 있는 가장 강력한 수단이라는 것은 이제 그 누구도 반박할 수 없을 것이다. 살아가는 삶의 양식을 바꾸는 세상의 문화들은 기독교인들을 비롯한 종교인들의 모든 행위들을 제약에 걸리게 했고, 그 문화들보가 강한 진리에 대해서 우리는 항상 탐구하고 기준을 설정해야 할 것이라고 생각된다. \n" +
            "기독교와 불교의 공통점\n" +
            "비움의 다름\n" +
            "불교-비움이 해탈의 수단\n" +
            "기독교- 비움이 하나님의 임재 수단\n" +
            "기독교와 문화의 위기원인\n" +
            "1.   진리의 이분화\n" +
            "유기체vs이분화\n" +
            "고전 12장-몸의 각 지체-문화와 진리의 상호보완 없이 계속되는 이분화\n" +
            "기독교가 역사에서 살아남음=기독교가 상황에 잘 적응하느냐?\n" +
            "시대에 맞는 기독교의 모습으로 바뀌어 상황을 주도하는 종교가 되어가야 한다.\n" +
            "하늘과 땅의통일=성과 속=영과육=영성과지성=학문과말씀\n" +
            "조각과와 석공\n" +
            "석공같은 조각가=속된 예술인, 조각가 같은 석공=성스러운 석공\n" +
            "->하나님의 창조세계에서 더 나은 삶을 만들고 더 나은 모습들을 만들어가는 모습들.\n" +
            "성스러운vs세상의것\n" +
            "수도사 로렌스\n" +
            "-맨발의 까르멜 수도회= 평생을 평수사로 일하며 샌들 수선하는 일, 하지만 하나님의 임재하심을 누리고 즐기면서 살아감.\n" +
            "무엇을 하는가, 어떻게 하는가의 의미가 매우 중요하다.\n" +
            "대상의 문제->태도의 문제= 칼보다 강한 것은 펜- 내가 처한 상황의 의미를 인정하고 깨닫는 것.\n" +
            "성과 속의 문제\n" +
            "=대상의 문제->태도의 문제\n" +
            "각자의 은사에 맞게 살아가는 것.\n" +
            "거룩함은 대상의 문제가 아니라 태도의문제이다.\n" +
            "최정훈 목사님 간증\n" +
            "사람이 대단하고 위대해서 어떠한 목적이 이뤄지는 것이 아니다.\n" +
            "->하나님께 의지하고 나의 낮음을 인정하는 태도가 만들어준다\n" +
            "목사님의 자부심이 대단하시다. 모든 스펙들을 자랑하는 것이 아니다,\n" +
            "-모든 것들은 하나님의 은혜이다 하나님께서 이루신 것이고 하나님께서 보이실 일들이다.\n" +
            "성(sex) = 거룩함\n" +
            "속 (secular) = 비거룩\n" +
            "둘다 고난과 즐거움이 있다. 하지만 그 고난과 즐거움의 속성은 상이하다. \n" +
            "이왕이면 하나님 나라를 위해 고통받자 그러면 하늘에 상급이 쌓인다.\n" +
            "쉽게 정리하면\n" +
            "거룩/비거룩 둘다 고난과 즐거움이 존재한다. 그러나 그 속성/본질은 다르다\n" +
            "거룩 = 하늘상급/세상에서의 놀림\n" +
            "비거룩 = 쾌락/죄책감, 지옥\n" +
            "대상의 문제 => 태도의 문제\n";

//    String text = "1. 건강과 사회복지\n" +
//            "건강- 신체적,정신적,사회적,영적 안녕이 완전한 상태\n" +
//            "질병문제- 개인 심리사회적 문제, 대인관계, 가족문제 등\n" +
//            "질병모델- 생의학적, 사회문화적, 심리스트레스적, 생리 심리사회적 등\n" +
//            "\n" +
//            "캐봇- 질병은 알지만 질병을 가진 인간을 알 수가 없음을 지적. (의료와 사회복지의 융합)\n" +
//            "\n" +
//            "2. 의료사회복지의 개념\n" +
//            "환자, 가족의 사회적 기능의 회복-치료-재활 다양한 목적 달성을 위한 사회복지실천활동\n" +
//            "-기술: 사회적, 경제적, 심리적 문제를 전문적인 원조로 돕는 전문적 활동,\n" +
//            "-정책: 질병의 예방과 건강증진을 목적으로 의료서비스를 평가하고 질적 향상을 도모\n" +
//            "\n" +
//            "3. 의료사회복지사의 역할과 직무\n" +
//            "의료사회복지의 대상: 질병 경험 대상자들과 가족\n" +
//            "질환 vs 질병 \n" +
//            "질환= 급성, 만성(장기적, 예후 불확실, 다양한 서비스 필요)\n" +
//            "질병= 환자 주관적 병의 경험.- 심리 사회학적 개념\n" +
//            "\n" +
//            "의료복지사의 역할: 외상 신체적 특징, 반응 양식 행동, 임상과별 특수질환에 대한 이해 필요\n" +
//            "\n" +
//            "의료복지의 대상\n" +
//            "(1)일반- 치료와의 관련성 재고, 치료 방해요소 제거, 전체적인 치료 및 재활에 개입\n" +
//            "(2)만성- 질환 수용 원조, 의료팀간의 중재, 퇴원 후 재활 원조\n" +
//            "(3)가족- 가족이 환자의 질환수용을 도움, 긍정적인 자원으로 작용\n" +
//            "\n" +
//            "사진으로 존재\n" +
//            "의료사회복지의 기능과 역할\n" +
//            "의료사회복지사의 직무체계\n" +
//            "의료사회복지서의 역할과 대상\n" +
//            "의료사회복지사업 법적 근거\n" +
//            "\n" +
//            "4. 의료사회복지 관련 제도\n" +
//            "(1) 의료보장제도\n" +
//            "-사회보험: 국민건강보험과 장애급여, 군인 치료보상 등\n" +
//            "-공공부조: 의료급여, 해산보로, 군인보훈보상법에 따른 수용치료 등\n" +
//            "(2) 국민건강보험\n" +
//            "- 강제보험의 원칙\n" +
//            "- 국민 건강보험법=전 국민이 건강보험의 대상\n" +
//            "(3) 의료급여\n" +
//            "- 의료급여대상자 선정\n" +
//            "- 의료혜택 전달\n" +
//            "\n" +
//            "5. 의료사회복지사의 자격제도와 전망\n" +
//            "(1) 의료사회복지사 자격제도- 국가공인은 x\n" +
//            "수련자격 및 내용- 40시간 이상 이론 960이상의 임상수련\n" +
//            "수련수퍼바이저- 학사 임상 5년이상 , 석사 3년이상\n" +
//            "수련기관의 조건- 수퍼바이저 상근, 사회복지 부서의 독립존재\n" +
//            "(2)의료사회복지의 전망\n" +
//            "그림으로 확인\n" +
//            "- 의료현장의 다양화: 의료복지서비스의 지역사회화, 시설,기과등의 확충필요\n" +
//            "-병원중심의 서비스에서 지식과 개입기술의 전문화가 요구됨\n" +
//            "(3) 의료사회복지의 향후과제\n" +
//            "- 지식과 기술이 현장의 욕구에 대응하여 강화되어야 함\n" +
//            "- 의료사회복지실천 서비스의 효과성을 입증할 연구능력의 강화\n" +
//            "- 슈퍼비전제도 강화(직원에 대한 엄격한 슈퍼비전)\n" +
//            "- 다양한 지역사회기관이 의료사회복지의 도입과 적용\n" +
//            "\n" +
//            "\n" +
//            "노인의 정의(브린)\n" +
//            "생리적측면에서 쇠퇴기에 있는 사람\n" +
//            "심리적 측면에서 쇠퇴기에 있는 사람\n" +
//            "사회적 측면에서 지위와 역할이 상실되어가는 사람\n" +
//            "\n" +
//            "노인의 정의 (사전적)\n" +
//            "1. 역연력\n" +
//            "- 일정 연령 이상을 노인으로 규정\n" +
//            "- 노화와 개인 차 간과의 문제 있음\n" +
//            "2. 사회적 역할에 의한 정의\n" +
//            "- 사회적 시간을 적용하여 사회적 역할과 지위를 고려하여 규정\n" +
//            "- 퇴직자, 주부등이 노인임\n" +
//            "3. 기능적 연령에 의한 정의\n" +
//            "- 신체기능, 생산적 기능을 근거로 규정\n" +
//            "- 특정 업무나 일을 수행할 수 없는 수준->역연령보다 상황을 고려\n" +
//            "4. 주관적 정의\n" +
//            "- 개인의 자각, 스스로가 자신을 규정\n" +
//            "\n" +
//            "노인의 조작적 정의\n" +
//            "수명의 연장으로 인하여 60세에서 65세 이상으로 기준의 변화\n" +
//            "고령화 7 고령 14 초고령 20\n" +
//            "\n" +
//            "노인의 신체적 특성\n" +
//            "1. 신체 2. 내부기관 3. 감각기능 의 변화\n" +
//            "\n" +
//            "노인의 인지적 특성\n" +
//            "기억력, 문제해결 능력.\n" +
//            "사고능력의 저하와 창의성의 퇴화.\n" +
//            "->노인의 인지적 능력은 개인 및 문화차에 따라 변화가능성이 있음\n" +
//            "\n" +
//            "노인의 심리적 특성\n" +
//            "- 성격변화: 흥미감퇴, 비활동적 경향, 경직성 증가, 수동성 증가 등\n" +
//            "- 프로이드, 융: 노년기=아동기 자기 중심적 성격으로 되돌아감.\n" +
//            "- 에릭슨 등: 현제의 제한점을 수용이후 만족한 삶을 영위\n" +
//            "\n" +
//            "노인의 사회적 특성\n" +
//            "은퇴이후 지위저하-> 수입감소-> 사회와 가정에서 권위약화-> 고독과 소외감\n" +
//            "\n" +
//            "노인문제\n" +
//            "(소득상실-경제적 빈곤, 건강상실-질병과 장애, 역할상실-소외, 관계상실-고독)\n" +
//            "\n" +
//            "사진으로 자료제시\n" +
//            "\n" +
//            "노인복지\n" +
//            "장인협회, 최성재- 노인에게 인간다운 생활을 유지할 수 있는 공적 및 사적 차원의 원조\n" +
//            "법적 정의- 노인의 질환을 조기발견, 보건복지 증진에 기여함을 목적\n" +
//            "노인복지의 원칙-사진으로 설명(29페이지)\n" +
//            "노인복지정책(소득보장)\n" +
//            "- 공적연금: 사회보험(공무원연급, 군인연급 등)\n" +
//            "- 공공부조: 국민기초생활보장제도, 기초노령연금\n" +
//            "- 간접적 소득보장제도: 경로우대제도, 각종 세제\n" +
//            "\n" +
//            "인간-감정적,인지적,생물학적,영적 체계\n" +
//            "체계간의 연결/결합, 사회적관념/문화적가치, 간점적인환경, 직접적인 환경\n" +
//            "->개인의 시간에 따른 변화\n" +
//            "\n" +
//            "1. 생태체계이론= 생태학적 이론+체계론\n" +
//            "- 체계= 개인,가족,집단,조직,공동체등의 사회체계\n" +
//            "체계들간의 관계\n" +
//            "투입: 다른 체계로부터 받는 에너지, 정보, 의사소통\n" +
//            "산출: 다른 체계로 방출되는 에너지, 정보, 의사소통\n" +
//            "체계= 투입과 산출과정에서 '항상성'유지의 경향이 있음\n" +
//            "- 생태학:인간과 환경의 상호작용\n" +
//            "적응,대처,상호의존\n" +
//            "\n" +
//            "2. 사회복지와 생태체계이론\n" +
//            "- 인간을 맥락 속에서 상호작용하는 것으로 판단.\n" +
//            "- 인간체계 상호작용의 중요성\n" +
//            "- 내부 혹은 외부 요인들에 대한 반응으로 발전\n" +
//            "\n" +
//            "3. 사례관리의 개념\n" +
//            "환경속에서 개인에게 필요한 서비스와 자원을 스스로 획득\n" +
//            "->사회적 기능을 원활히 수행하게 도움.\n" +
//            "사례관리의 등장배경\n" +
//            "사회복지의 방향- 지역사회복지 중심\n" +
//            "->재가복지서비스의 확장이 이루어짐.\n" +
//            "사례관리의 특성(13p)\n" +
//            "사례관리의 구성(14p)\n" +
//            "사례관리의 과정(15p)\n" +
//            "\n" +
//            "4. 지역사회복지\n" +
//            "정의= 기능 혹은 이해관계나 정체성을 공유하는 집단.\n" +
//            "-영토성, 사회적 상호작용, 서로 공유하는 끈\n" +
//            "의미와 유형\n" +
//            "(1) 지리적의미=행정적, 자연적의미\n" +
//            "(2) 기능적의미= 인종적,성,직업,장애 등 특성에 의미\n" +
//            "지역사회+사회복지\n" +
//            "지역사회를 접근 단위로 하여 복지실현을 고취-> 지역사회의 변화\n" +
//            "세가지 관점\n" +
//            "1. 사회복지실천 환경으로서의 지역사회\n" +
//            "2. 변화와 노력의 대상으로서의 지역사회\n" +
//            "3. 변화를 위한 기제로서의 지역사회\n" +
//            "실천과정과 개발모델(24.25.26.27)";

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
                        "5. List keywords by importance without numbering. Importance criteria: uncommon words, strongly related to the text's theme, and core to understanding the text.\n" +
                        "6. Ensure no duplicate keywords. If duplicates are found, replace them with other unique keywords to meet the required count.\n" +
                        "7. Provide exactly %d unique keywords in one proper noun form each.\n" +
                        "8. If a keyword contains foreign words written in Korean, output only the foreign word without any Korean particles.\n" +
                        "9. Output can be in both Korean and English.\n" +
                        "10. If the text is code, extract keywords related to programming syntax.\n" +
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


        log.info("\nKeywords: " + keywords);

        String formattedKeywords = formatKeywordsAsList(keywords);

        openAIService.setKeywords(keywords);

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

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonResponse = mapper.createObjectNode();
//        jsonResponse.put("Content", text);
        jsonResponse.put("formattedKeywords", formattedKeywords);
//        jsonResponse.put("replacedText", replacedText.toString());
//        jsonResponse.putPOJO("Answer", arr);


        // ###########################################################################
        String prompt1 = String.format(
                "### Instruction ###\n" +
                        "Based on the provided text, generate 20 questions and answers focusing on key concepts.\n" +
                        "The purpose is to verify understanding of the text by learners.\n" +
                        "### Format ###\n" +
                        "1. Question: Formulate a question that allows the key concept (answer) to be inferred. Questions should be of a difficulty level suitable for university students.\n" +
                        "문제의 난이도: 대학원생, 교수에 맞게" +
                        "2. Answer: You must select answers from the provided keywords: %s\n" +
                        "Answers should not be duplicated."+
                        "Use the exact form of the words in the list as the answer without any changes.\n" +
                        "### Example ###\n" +
                        "Text: '기독교가 역사에서 살아남음=기독교가 상황에 잘 적응하느냐?'\n" +
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
                        "### Text ###\n" +
                        "%s",
                String.join(", ", keywords), text
        );



        ChatGPTRequest request1 = new ChatGPTRequest(model, prompt1);

        ChatGPTResponse response1 = restTemplate.postForObject(apiURL, request1, ChatGPTResponse.class);

        if (response1 != null && response1.getChoices() != null && !response1.getChoices().isEmpty()) {
            String content = response1.getChoices().get(0).getMessage().getContent();
            log.info("Content:\n" + content);
        } else {
            log.info("GPT API한테서 받아온 데이터 없음.");
        }

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

