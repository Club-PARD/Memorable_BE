package com.study.memorable.WorkSheet.controller;

import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        workSheetService.delete(id);
        return "잘 삭제됨!";
    }

    @PostMapping("/logWorkSheetClick")
    public void logWorkSheetClick(@RequestParam Long userId, @RequestParam Long workSheetId) {
        workSheetService.logWorkSheetClick(userId, workSheetId);
    }

    @GetMapping("/lastClickedWorkSheet")
    public Long getLastClickedWorkSheet(@RequestParam Long userId) {
        return workSheetService.getLastClickedWorkSheetId(userId);
    }
}
