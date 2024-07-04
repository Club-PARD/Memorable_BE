package com.study.memorable.TestSheet.controller;

import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.service.TestSheetService;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/testsheets")
public class TestSheetController {

    private final TestSheetService testSheetService;

    @PostMapping("")
    public TestSheetReadDTO createTestSheet(@RequestBody TestSheetCreateDTO dto) {
        return testSheetService.createTestSheet(dto);
    }
}
