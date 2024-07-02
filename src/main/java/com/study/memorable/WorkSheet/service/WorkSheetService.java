package com.study.memorable.WorkSheet.service;

import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import com.study.memorable.WorkSheet.dto.WorkSheetCreateDTO;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import com.study.memorable.WorkSheet.entity.WorkSheetClickLog;
import com.study.memorable.WrongSheet.repo.WorkSheetClickLogRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkSheetService {
    private final WorkSheetRepo workSheetRepo;
    private final WorkSheetClickLogRepo workSheetClickLogRepo;
    private final UserRepo userRepo;

    public void createWorkSheet(WorkSheetCreateDTO dto){
        workSheetRepo.save(new WorkSheet().toEntity(dto));
    }

    public List<WorkSheetReadDTO> findAll(){
        return workSheetRepo.findAll()
                .stream()
                .map(WorkSheetReadDTO::toDTO)
                .collect(Collectors.toList());
    }


    public void delete(Long id){
        workSheetRepo.deleteById(id);
    }


    public void logWorkSheetClick(Long userId, Long workSheetId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        WorkSheet workSheet = workSheetRepo.findById(workSheetId).orElseThrow(() -> new RuntimeException("WorkSheet not found"));

        WorkSheetClickLog clickLog = WorkSheetClickLog.builder()
                .user(user)
                .workSheet(workSheet)
                .build();

        workSheetClickLogRepo.save(clickLog);
    }

    public Long getLastClickedWorkSheetId(Long userId) {
        Optional<WorkSheetClickLog> lastClickLog = workSheetClickLogRepo.findTopByUserIdOrderByClickTimeDesc(userId);
        return lastClickLog.map(log -> log.getWorkSheet().getId()).orElse(null);
    }

}
