package com.study.memorable.WorkSheet.service;

import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkSheetService {
    private final WorkSheetRepo workSheetRepo;

    public void createWorkSheet(WorkSheetCreateDTO dto){
        workSheetRepo.save(new WorkSheet().toEntity(dto));
    }

    public List<WorkSheetReadDTO> findAll(){
        return workSheetRepo.findAll()
                .stream()
                .map(WorkSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }

}
