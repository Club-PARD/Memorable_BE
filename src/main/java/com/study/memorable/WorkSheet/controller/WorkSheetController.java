package com.study.memorable.WorkSheet.controller;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worksheet")
public class WorkSheetController {
    private final WorkSheetService workSheetService;

    @PostMapping("")
    public ResponseEntity<WorkSheetReadDTO> createWorkSheet(@RequestBody FileCreateDTO dto) {
        WorkSheetReadDTO workSheetReadDTO = workSheetService.createWorkSheet(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(workSheetReadDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WorkSheetReadDTO>> getWorkSheetsByUserId(@PathVariable String userId) {
        List<WorkSheetReadDTO> workSheets = workSheetService.getWorkSheetsByUserId(userId);
        if (workSheets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(workSheets);
    }

    @GetMapping("/ws/{worksheetId}")
    public ResponseEntity<WorkSheetReadDTO> getWorkSheetById(@PathVariable Long worksheetId) {
        WorkSheetReadDTO worksheet = workSheetService.getFullWorkSheetById(worksheetId);
        if (worksheet != null) {
            return ResponseEntity.ok(worksheet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recentDate/{userId}") // 가장 최신 worksheet 반환
    public ResponseEntity<WorkSheetReadDTO> getMostRecentWorksheetByUserId(@PathVariable String userId) {
        WorkSheetReadDTO mostRecentWorksheet = workSheetService.getMostRecentWorksheetByUserId(userId);
        if (mostRecentWorksheet != null) {
            return ResponseEntity.ok(mostRecentWorksheet);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/done/{worksheetId}") // isCompleteAllBlanks -> TRUE
    public ResponseEntity<WorkSheetReadDTO> toggleIsCompleteAllBlanks(@PathVariable Long worksheetId) {
        WorkSheetReadDTO updatedWorksheet = workSheetService.toggleIsCompleteAllBlanks(worksheetId);
        return ResponseEntity.ok(updatedWorksheet);
    }

    @PatchMapping("/make/{worksheetId}") // isMakeTestSheet -> TRUE
    public ResponseEntity<WorkSheetReadDTO> makeWorksheet(@PathVariable Long worksheetId) {
        WorkSheetReadDTO updatedWorksheet = workSheetService.makeWorksheet(worksheetId);
        return ResponseEntity.ok(updatedWorksheet);
    }

    @PatchMapping("/addsheet/{worksheetId}") // isAddWorksheet -> TRUE
    public ResponseEntity<WorkSheetReadDTO> toggleAddWorksheet(@PathVariable Long worksheetId) {
        WorkSheetReadDTO updatedWorksheet = workSheetService.toggleAddWorksheet(worksheetId);
        return ResponseEntity.ok(updatedWorksheet);
    }

    @PatchMapping("/{worksheetId}") // bookmark (TRUE/FALSE) 토글
    public ResponseEntity<WorkSheetReadDTO> toggleBookmark(@PathVariable Long worksheetId) {
        WorkSheetReadDTO updatedWorksheet = workSheetService.toggleBookmark(worksheetId);
        return ResponseEntity.ok(updatedWorksheet);
    }

    // 7. Update recent date for a worksheet
    @PatchMapping("/recentDate/{worksheetId}") // 특정 worksheet의 recentDate 수정
    public ResponseEntity<WorkSheetReadDTO> updateRecentDate(@PathVariable Long worksheetId) {
        WorkSheetReadDTO updatedWorksheet = workSheetService.updateRecentDate(worksheetId);
        return ResponseEntity.ok(updatedWorksheet);
    }

    @PatchMapping("/edit/{worksheetId}") // 파일명 수정
    public ResponseEntity<String> updateFileName(@PathVariable Long worksheetId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        if (name != null && !name.isEmpty()) {
            workSheetService.updateFileName(worksheetId, name);
            return ResponseEntity.ok("✅File Name Updated!");
        } else {
            return ResponseEntity.badRequest().body("⚠️File name cannot be empty");
        }
    }

    @DeleteMapping("/{worksheetId}") // worksheet 삭제
    public ResponseEntity<Void> deleteWorksheet(@PathVariable Long worksheetId) {
        workSheetService.deleteWorksheet(worksheetId);
        return ResponseEntity.noContent().build();
    }
}
