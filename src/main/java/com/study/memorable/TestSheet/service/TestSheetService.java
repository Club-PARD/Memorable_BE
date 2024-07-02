package com.study.memorable.TestSheet.service;

import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.TestSheet.dto.TestSheetCreateDTO;
import com.study.memorable.TestSheet.dto.TestSheetReadDTO;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.TestSheet.repo.TestSheetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestSheetService {
    private final TestSheetRepo testSheetRepo;
    private final FileRepo fileRepo;

    public TestSheetReadDTO createTestSheet(TestSheetCreateDTO dto, Long fileId) {
        File file = fileRepo.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));

        TestSheet testSheet = new TestSheet().toEntity(dto);
        testSheet.setFile(file);

        TestSheet savedTestSheet = testSheetRepo.save(testSheet);
        return TestSheetReadDTO.toDTO(savedTestSheet);
    }

    public List<TestSheetReadDTO> findAll(){
        return testSheetRepo.findAll()
                .stream()
                .map(TestSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }
}
