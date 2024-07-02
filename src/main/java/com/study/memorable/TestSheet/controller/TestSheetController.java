package com.study.memorable.TestSheet.controller;

import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.service.TestSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ts")
public class TestSheetController {

    private final TestSheetService testSheetService;

    @PostMapping("")
    public String createTestSheet(TestSheetCreateDTO dto){
        testSheetService.createTestSheet(dto);
        return "시험지 생성됨!";
    }

    @GetMapping("")
    public void findAll(){
        testSheetService.findAll();
    }
}
