package com.study.memorable.TestSheet.controller;

import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.service.TestSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/testsheet")
public class TestSheetController {

    private final TestSheetService testSheetService;

    @PostMapping("/{worksheetId}")
    public TestSheetReadDTO createTestSheet(@PathVariable Long worksheetId) {
        return testSheetService.createTestSheet(worksheetId);
    }
}
