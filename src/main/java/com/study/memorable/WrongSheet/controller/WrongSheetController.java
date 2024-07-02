package com.study.memorable.WrongSheet.controller;

import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.service.WrongSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wrongsheet")
public class WrongSheetController {

    private final WrongSheetService wrongSheetService;

    @PostMapping("")
    public String createWrongSheet(WrongSheetCreateDTO dto) {
        wrongSheetService.createWrongSheet(dto);
        return "오답지 생성됨!";
    }

    @GetMapping("")
    public void findAll() {
        wrongSheetService.findAll();
    }


}
