package com.study.memorable.WrongSheet.controller;

import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetReadDTO;
import com.study.memorable.WrongSheet.service.WrongSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wrongsheet")
public class WrongSheetController {

    private final WrongSheetService wrongSheetService;

    @PostMapping("")
    public String createWrongSheet(@RequestBody WrongSheetCreateDTO dto) {
        wrongSheetService.createWrongSheetAndReturn(dto);
        return "오답지 생성됨!";
    }

    @GetMapping("/{userId}")
    public List<WrongSheetReadDTO> getWrongSheetsByUserId(@PathVariable String userId) {
        return wrongSheetService.findByUserId(userId);
    }

    @GetMapping("/{wrongsheetId}")
    public WrongSheetReadDTO getWrongSheetById(@PathVariable Long wrongsheetId) {
        return wrongSheetService.findWrongSheetById(wrongsheetId);
    }

    @PatchMapping("/{wrongsheetId}")
    public WrongSheetReadDTO updateWrongSheet(@PathVariable Long wrongsheetId, @RequestBody WrongSheetCreateDTO dto) {
        return wrongSheetService.updateWrongSheet(wrongsheetId, dto);
    }

    @DeleteMapping("/{wrongsheetId}")
    public void deleteWrongSheet(@PathVariable Long wrongsheetId) {
        wrongSheetService.deleteWrongSheet(wrongsheetId);
    }
}
