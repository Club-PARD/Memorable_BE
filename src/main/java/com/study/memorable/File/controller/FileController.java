package com.study.memorable.File.controller;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("")
    public FileReadDTO createFileAndWorksheet(@RequestBody FileCreateDTO dto) {
        return fileService.createFileAndWorksheet(dto);
    }

    @GetMapping("")
    public List<FileReadDTO> findAll() {
        return fileService.findAll();
    }

    @GetMapping("/{id}")
    public FileReadDTO findFileById(@PathVariable Long id) {
        return fileService.findFileById(id);
    }

//    @GetMapping("/logOddKeywords/{fileId}")
//    public String logOddIndexKeywords(@PathVariable Long fileId) {
//        fileService.logOddIndexKeywords(fileId);
//        return "키워드 2개로 나눠서 추출 성공!";
//    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        fileService.deleteById(id);
        return "파일 삭제됨!";
    }
}
