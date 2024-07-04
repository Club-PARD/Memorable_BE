package com.study.memorable.WorkSheet.service;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import com.study.memorable.OpenAI.controller.OpenAIController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkSheetService {
    private final WorkSheetRepo workSheetRepo;
    private final FileRepo fileRepo;
    private final UserRepo userRepo;
    private final OpenAIController openAIController;

    @Transactional
    public WorkSheetReadDTO createWorkSheet(FileCreateDTO dto) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        File file = fileRepo.save(File.builder()
                .file_name(dto.getFileName())
                .category(dto.getCategory())
                .content(dto.getContent())
                .keyword(openAIController.extractKeywordsFromContent(dto.getContent()))
                .created_date(LocalDateTime.now())
                .user(user)
                .build());

        WorkSheet workSheet = WorkSheet.builder()
                .file(file)
                .bookmark(false)
                .isCompleteAllBlanks(false)
                .isReExtracted(false)
                .answer1(openAIController.processKeywords(file.getOddIndexKeywords()))
                .answer2(openAIController.processKeywords(file.getEvenIndexKeywords()))
                .created_date(LocalDateTime.now())
                .recent_date(LocalDateTime.now())
                .name(dto.getFileName())
                .build();
        workSheetRepo.save(workSheet);

        return WorkSheetReadDTO.toFullDTO(workSheet);
    }

    @Transactional(readOnly = true)
    public List<WorkSheetReadDTO> getWorkSheetsByUserId(String userId) {
        return fileRepo.findByUser_Id(String.valueOf(Long.valueOf(userId))).stream()
                .flatMap(file -> file.getWorksheets().stream())
                .map(WorkSheetReadDTO::toBasicDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public WorkSheetReadDTO getFullWorkSheetById(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        return WorkSheetReadDTO.toFullDTO(worksheet);
    }

    @Transactional
    public WorkSheetReadDTO toggleBookmark(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        worksheet.setBookmark(!worksheet.isBookmark());
        workSheetRepo.save(worksheet);
        return WorkSheetReadDTO.toBasicDTO(worksheet);
    }

    @Transactional
    public void updateRecentDate(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        worksheet.setRecent_date(LocalDateTime.now());
        workSheetRepo.save(worksheet);
    }

    @Transactional(readOnly = true)
    public WorkSheetReadDTO getMostRecentWorksheetByUserId(String userId) {
        return fileRepo.findByUser_Id(String.valueOf(Long.valueOf(userId))).stream()
                .flatMap(file -> file.getWorksheets().stream())
                .max(Comparator.comparing(WorkSheet::getRecent_date))
                .map(WorkSheetReadDTO::toFullDTO)
                .orElseThrow(() -> new RuntimeException("No worksheets found for user"));
    }

    @Transactional
    public void deleteWorksheet(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        workSheetRepo.delete(worksheet);
    }
}
