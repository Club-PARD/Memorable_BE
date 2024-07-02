package com.study.memorable.TestSheet.controller;

import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.service.TestSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ts")
public class TestSheetController {

    private final TestSheetService testSheetService;

    @PostMapping("")
    public String createTestSheet(@RequestBody TestSheetCreateDTO dto, @RequestParam Long fileId) {
        testSheetService.createTestSheet(dto, fileId);
        return "시험지 생성됨!";
    }

    @GetMapping("")
    public void findAll() {
        testSheetService.findAll();
    }
}
