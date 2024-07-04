package com.study.memorable.WorkSheet.controller;

import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
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
    public WorkSheetReadDTO createWorksheet(@RequestBody WorkSheetCreateDTO dto) {
        return workSheetService.createWorksheetAndReturn(dto);
    }


}



