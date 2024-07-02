package com.study.memorable.WrongSheet.service;

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

    public void createWrongSheet(WrongSheetCreateDTO dto) {
        wrongSheetRepo.save(new WrongSheet().toEntity(dto));
    }

    public List<WrongSheetReadDTO> findAll() {
        return wrongSheetRepo.findAll()
                .stream()
                .map(WrongSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }
}