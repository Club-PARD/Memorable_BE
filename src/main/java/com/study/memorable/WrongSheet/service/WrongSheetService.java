package com.study.memorable.WrongSheet.service;

import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.TestSheet.repo.TestSheetRepo;
import com.study.memorable.WrongSheet.dto.WrongSheetCreateDTO;
import com.study.memorable.WrongSheet.dto.WrongSheetReadDTO;
import com.study.memorable.WrongSheet.entity.WrongSheet;
import com.study.memorable.WrongSheet.repo.WrongSheetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WrongSheetService {
    private final WrongSheetRepo wrongSheetRepo;
    private final TestSheetRepo testSheetRepo;

    public List<WrongSheetReadDTO> findByUserId(String userId) {
        return testSheetRepo.findByFile_User_Id(userId).stream()
                .flatMap(testSheet -> testSheet.getWrongSheets().stream())
                .map(WrongSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }

    public WrongSheetReadDTO findWrongSheetById(Long id) {
        WrongSheet wrongSheet = wrongSheetRepo.findById(id).orElseThrow(() -> new RuntimeException("WrongSheet not found"));
        return WrongSheetReadDTO.toDTO(wrongSheet);
    }

    public void createWrongSheetAndReturn(WrongSheetCreateDTO dto) {
        TestSheet testSheet = testSheetRepo.findById(dto.getTestSheetId())
                .orElseThrow(() -> new RuntimeException("TestSheet not found"));
        WrongSheet wrongSheet = WrongSheet.toEntity(dto, testSheet);
        wrongSheetRepo.save(wrongSheet);
        WrongSheetReadDTO.toDTO(wrongSheet);
    }

    public WrongSheetReadDTO updateWrongSheet(Long wrongSheetId, WrongSheetCreateDTO dto) {
        WrongSheet wrongSheet = wrongSheetRepo.findById(wrongSheetId)
                .orElseThrow(() -> new RuntimeException("WrongSheet not found"));
        wrongSheetRepo.save(wrongSheet);
        return WrongSheetReadDTO.toDTO(wrongSheet);
    }

    public void deleteWrongSheet(Long wrongSheetId) {
        wrongSheetRepo.deleteById(wrongSheetId);
    }
}
