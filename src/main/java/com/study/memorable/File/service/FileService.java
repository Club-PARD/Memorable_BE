package com.study.memorable.File.service;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;

    public void createFile(FileCreateDTO dto){
        fileRepo.save(new File().toEntity(dto));
    }

    public List<FileReadDTO> findAll(){
        return fileRepo.findAll()
                .stream()
                .map(FileReadDTO::toDTO)
                .collect(Collectors.toList());
    }

}
