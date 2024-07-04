package com.study.memorable.WorkSheet.service;

import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkSheetService {
    private final WorkSheetRepo workSheetRepo;
    private final FileRepo fileRepo;

    public List<WorkSheetReadDTO> findByUserId(String userIdentifier) {
        List<File> files = fileRepo.findByUser_Id(userIdentifier);
        return files.stream()
                .flatMap(file -> file.getWorksheets().stream())
                .map(WorkSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }

    public WorkSheetReadDTO findWorkSheetById(Long id) {
        WorkSheet worksheet = workSheetRepo.findById(id).orElseThrow(() -> new RuntimeException("Worksheet not found"));
        return WorkSheetReadDTO.toDTO(worksheet);
    }
}
