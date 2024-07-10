package com.study.memorable.WorkSheet.controller;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public WorkSheetReadDTO updateRecentDate(@PathVariable Long worksheetId) {
        return workSheetService.updateRecentDate(worksheetId);
    }

    @GetMapping("/recentDate/{userId}")
    public WorkSheetReadDTO getMostRecentWorksheetByUserId(@PathVariable String userId) {
        return workSheetService.getMostRecentWorksheetByUserId(userId);
    }

    @PatchMapping("/done/{worksheetId}")
    public WorkSheetReadDTO toggleIsCompleteAllBlanks(@PathVariable Long worksheetId) {
        return workSheetService.toggleIsCompleteAllBlanks(worksheetId);
    }

    @PatchMapping("/addsheet/{worksheetId}")
    public WorkSheetReadDTO toggleAddWorksheet(@PathVariable Long worksheetId) {
        return workSheetService.toggleAddWorksheet(worksheetId);
    }

    @PatchMapping("/make/{worksheetId}")
    public WorkSheetReadDTO makeWorksheet(@PathVariable Long worksheetId) {
        return workSheetService.makeWorksheet(worksheetId);
    }

    @PatchMapping("/edit/{worksheetId}")
    public String updateFileName(@PathVariable Long worksheetId, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        workSheetService.updateFileName(worksheetId, name);
        return "File NameUpdated!";
    }


    @DeleteMapping("/{worksheetId}")
    public void deleteWorksheet(@PathVariable Long worksheetId) {
        workSheetService.deleteWorksheet(worksheetId);
    }
}
