package com.study.memorable.WorkSheet.controller;

import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("worksheet")
public class WorkSheetController {
    private final WorkSheetService workSheetService;

    @PostMapping("")
    public String createWorkSheet(WorkSheetCreateDTO dto) {
        workSheetService.createWorkSheet(dto);
        return "빈칸 학습지 생성됨!";
    }

    @GetMapping("")
    public List<WorkSheetReadDTO> findAll(){
        return workSheetService.findAll();
    }
}
