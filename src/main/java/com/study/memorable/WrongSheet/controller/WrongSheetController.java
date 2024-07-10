package com.study.memorable.WrongSheet.controller;

import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetResponseDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetSimpleReadDTO;
import com.study.memorable.WrongSheet.service.WrongSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wrongsheet")
public class WrongSheetController {

    private final WrongSheetService wrongSheetService;

    @PostMapping("")
    public ResponseEntity<WrongSheetResponseDTO> createWrongSheet(@RequestBody WrongSheetCreateDTO dto) {
        WrongSheetResponseDTO response = wrongSheetService.createWrongSheet(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WrongSheetSimpleReadDTO>> getWrongSheetsByUserId(@PathVariable String userId) {
        List<WrongSheetSimpleReadDTO> response = wrongSheetService.getWrongSheetsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{wrongsheetId}")
    public ResponseEntity<WrongSheetResponseDTO> getWrongSheetById(@PathVariable Long wrongsheetId) {
        WrongSheetResponseDTO response = wrongSheetService.getWrongSheetById(wrongsheetId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{wrongsheetId}")
    public WrongSheetSimpleReadDTO toggleBookmark(@PathVariable Long wrongsheetId) {
        return wrongSheetService.toggleBookmark(wrongsheetId);
    }

    @PatchMapping("/edit/{wrongsheetId}")
    public String updateFileName(@PathVariable Long wrongsheetId, @RequestBody Map<String, String> request){
        String name = request.get("name");
        wrongSheetService.updateFileName(wrongsheetId, name);
        return "File Name, Updated!";
    }

    @DeleteMapping("/{wrongsheetId}")
    public void deleteWrongSheet(@PathVariable Long wrongsheetId) {
        wrongSheetService.deleteWrongSheet(wrongsheetId);
    }
}
