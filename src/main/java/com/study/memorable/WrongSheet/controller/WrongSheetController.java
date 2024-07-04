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

    @DeleteMapping("/{wrongsheetId}")
    public void deleteWrongSheet(@PathVariable Long wrongsheetId) {
        wrongSheetService.deleteWrongSheet(wrongsheetId);
    }
}
