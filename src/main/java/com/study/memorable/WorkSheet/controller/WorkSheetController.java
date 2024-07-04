package com.study.memorable.WorkSheet.controller;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/worksheet")
public class WorkSheetController {
    private final WorkSheetService workSheetService;

    @PostMapping("")
    public WorkSheetReadDTO createWorkSheet(@RequestBody FileCreateDTO dto) {
        return workSheetService.createWorkSheet(dto);
    }

    @GetMapping("/user/{userId}")
    public List<WorkSheetReadDTO> getWorkSheetsByUserId(@PathVariable String userId) {
        return workSheetService.getWorkSheetsByUserId(userId);
    }

    @GetMapping("/ws/{worksheetId}")
    public WorkSheetReadDTO getWorkSheetById(@PathVariable Long worksheetId) {
        return workSheetService.getFullWorkSheetById(worksheetId);
    }

    @PatchMapping("/{worksheetId}")
    public WorkSheetReadDTO toggleBookmark(@PathVariable Long worksheetId) {
        return workSheetService.toggleBookmark(worksheetId);
    }

    @PatchMapping("/recentDate/{worksheetId}")
    public void updateRecentDate(@PathVariable Long worksheetId) {
        workSheetService.updateRecentDate(worksheetId);
    }

    @GetMapping("/recentDate/{userId}")
    public WorkSheetReadDTO getMostRecentWorksheetByUserId(@PathVariable String userId) {
        return workSheetService.getMostRecentWorksheetByUserId(userId);
    }

    @DeleteMapping("/{worksheetId}")
    public void deleteWorksheet(@PathVariable Long worksheetId) {
        workSheetService.deleteWorksheet(worksheetId);
    }
}
