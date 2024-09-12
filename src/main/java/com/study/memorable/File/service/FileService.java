package com.study.memorable.File.service;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import com.study.memorable.WorkSheet.service.WorkSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;
    private final UserRepo userRepo;
    private final WorkSheetService workSheetService;

    public FileReadDTO createFileAndWorksheet(FileCreateDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        File file = File.toEntity(dto, user);
        fileRepo.save(file);

        return FileReadDTO.toDTO(file);
    }

    public List<FileReadDTO> findAll() {
        return fileRepo.findAll()
                .stream()
                .map(FileReadDTO::toDTO)
                .collect(Collectors.toList());
    }

    public FileReadDTO findFileById(Long id) {
        File file = fileRepo.findById(id).orElseThrow(() -> new RuntimeException("File not found"));
        return FileReadDTO.toDTO(file);
    }

    public void deleteById(Long id) {
        fileRepo.deleteById(id);
    }

//    public void logOddIndexKeywords(Long fileId) {
//        File file = fileRepo.findById(fileId).orElseThrow(() -> new RuntimeException("File not found"));
//        file.tests();
//    }
}
