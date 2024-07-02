package com.study.memorable.TestSheet.service;

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

    public void createTestSheet(TestSheetCreateDTO dto){
        testSheetRepo.save(new TestSheet().toEntity(dto));
    }

    public List<TestSheetReadDTO> findAll(){
        return testSheetRepo.findAll()
                .stream()
                .map(TestSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }
}
