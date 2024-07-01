package com.study.memorable.File.controller;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    @PostMapping("")
    public String createFile(@RequestBody FileCreateDTO dto){
        fileService.createFile(dto);
        return "파일 생성됨!";
    }

    @GetMapping("")
    public List<FileReadDTO> findAll(){
        return fileService.findAll();
    }




}


