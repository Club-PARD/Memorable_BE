package com.study.memorable.TestSheet.controller;

import com.study.memorable.TestSheet.dto.TestSheetDetailReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.dto.TestSheetSimpleReadDTO;
import com.study.memorable.TestSheet.service.TestSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/testsheet")
public class TestSheetController {

    private final TestSheetService testSheetService;

    @PostMapping("/{worksheetId}")
    public TestSheetReadDTO createTestSheet(@PathVariable Long worksheetId) {
        return testSheetService.createTestSheet(worksheetId);
    }

    @GetMapping("/user/{userId}")
    public List<TestSheetSimpleReadDTO> getTestSheetsByUserId(@PathVariable String userId) {
        return testSheetService.getTestSheetsByUserId(userId);
    }

    @GetMapping("/{testsheetId}")
    public TestSheetDetailReadDTO getTestSheetById(@PathVariable Long testsheetId) {
        return testSheetService.getTestSheetById(testsheetId);
    }


    @PatchMapping("/{testsheetId}")
    public ResponseEntity<Void> updateUserAnswers(
            @PathVariable Long testsheetId,
            @RequestBody Map<String, Object> userAnswers) {
        boolean isReExtracted = (boolean) userAnswers.get("isReExtracted");
        List<Boolean> isCompleteAllBlanks = (List<Boolean>) userAnswers.get("isCompleteAllBlanks");
        List<String> userAnswers1 = (List<String>) userAnswers.get("userAnswers1");
        List<String> userAnswers2 = (List<String>) userAnswers.get("userAnswers2");

        testSheetService.updateUserAnswers(testsheetId, isReExtracted, isCompleteAllBlanks, userAnswers1, userAnswers2);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/bookmark/{testsheetId}")
    public ResponseEntity<TestSheetSimpleReadDTO> updateTestSheetBookmark(@PathVariable Long testsheetId) {
        TestSheetSimpleReadDTO updatedTestSheet = testSheetService.updateTestSheetBookmark(testsheetId);
        return ResponseEntity.ok(updatedTestSheet);
    }

    @DeleteMapping("/{testsheetId}")
    public void deleteTestSheet(@PathVariable Long testsheetId) {
        testSheetService.deleteTestSheet(testsheetId);
    }
}