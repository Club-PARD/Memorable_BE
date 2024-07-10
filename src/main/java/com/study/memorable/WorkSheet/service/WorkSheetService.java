package com.study.memorable.WorkSheet.service;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.TestSheet.entity.TestSheet;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import com.study.memorable.WorkSheet.dto.WorkSheetReadDTO;
import com.study.memorable.WorkSheet.entity.WorkSheet;
import com.study.memorable.WorkSheet.repo.WorkSheetRepo;
import com.study.memorable.OpenAI.controller.OpenAIController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SqlFragmentAlias;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        String content = dto.getContent().replace("\n", " ");

        File file = fileRepo.save(File.builder()
                .file_name(dto.getName())
                .category(dto.getCategory())
                .content(content)
                .keyword(openAIController.extractKeywordsFromContent(content))
                .created_date(LocalDateTime.now())
                .user(user)
                .build());


        List<String> keywords = file.getKeyword();

        List<String> oddKeywords = getKeywordsByIndex(keywords, true);
        List<String> evenKeywords = getKeywordsByIndex(keywords, false);

        // GPT API -> 키워드로부터 문제와 답을 생성
        List<String> answer1 = generateAnswersFromKeywords(oddKeywords, file.getContent());
        List<String> answer2 = generateAnswersFromKeywords(evenKeywords, file.getContent());

        WorkSheet workSheet = WorkSheet.builder()
                .file(file)
                .bookmark(false)
                .isCompleteAllBlanks(false)
                .isAddWorksheet(false)
                .answer1(answer1)
                .answer2(answer2)
                .created_date(LocalDateTime.now())
                .recent_date(LocalDateTime.now())
                .build();
        workSheetRepo.save(workSheet);

        return WorkSheetReadDTO.toFullDTO(workSheet);
    }

    private List<String> getKeywordsByIndex(List<String> keywords, boolean isOdd) {
        List<String> result = new ArrayList<>();
        log.info("\n\n");
        for (int i = 0; i < keywords.size(); i++) {
            if ((i % 2 == 0 && !isOdd) || (i % 2 == 1 && isOdd)) {
                result.add(keywords.get(i));
                log.info("내가 뽑은 keywords: " + keywords.get(i));
            }
        }
        return result;
    }

    private List<String> generateAnswersFromKeywords(List<String> keywords, String content) {
        List<String> arr = new ArrayList<>();
//        StringBuilder replacedText = new StringBuilder();

        String[] words = content.split("\\s+");

        for (String word : words) {
//            boolean isKeyword = false;
            for (String key : keywords) {
                if (word.contains(key)) {
//                    replacedText.append(word.replace(key, "_____")).append(" ");
                    arr.add(key);
//                    isKeyword = true;
                    break;
                }
            }
//            if (!isKeyword) {
//                replacedText.append(word).append(" ");
//            }
        }

        return arr;
    }

    @Transactional(readOnly = true)
    public List<WorkSheetReadDTO> getWorkSheetsByUserId(String userId) {
        return fileRepo.findByUser_Id(userId).stream()
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
    public WorkSheetReadDTO updateRecentDate(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        worksheet.setRecent_date(LocalDateTime.now());
        workSheetRepo.save(worksheet);
        return WorkSheetReadDTO.toFullDTO(worksheet);
    }

    @Transactional(readOnly = true)
    public WorkSheetReadDTO getMostRecentWorksheetByUserId(String userId) {
        return fileRepo.findByUser_Id(userId).stream()
                .flatMap(file -> file.getWorksheets().stream())
                .max(Comparator.comparing(WorkSheet::getRecent_date))
                .map(WorkSheetReadDTO::toFullDTO)
                .orElseThrow(() -> new RuntimeException("No worksheets found for user"));
    }

    @Transactional
    public WorkSheetReadDTO toggleIsCompleteAllBlanks(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        worksheet.setCompleteAllBlanks(true);
        workSheetRepo.save(worksheet);
        return WorkSheetReadDTO.toFullDTO(worksheet);
    }

    @Transactional
    public WorkSheetReadDTO toggleAddWorksheet(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        worksheet.setAddWorksheet(true);
        workSheetRepo.save(worksheet);
        return WorkSheetReadDTO.toFullDTO(worksheet);
    }

    @Transactional
    public WorkSheetReadDTO makeWorksheet(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));

        worksheet.setMakeTestSheet(true);
        workSheetRepo.save(worksheet);

        return WorkSheetReadDTO.toFullDTO(worksheet);
    }

    @Transactional
    public void updateFileName(Long worksheetId, String name) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        File file = worksheet.getFile();
        file.setFile_name(name);
        fileRepo.save(file);
    }

    @Transactional
    public void deleteWorksheet(Long worksheetId) {
        WorkSheet worksheet = workSheetRepo.findById(worksheetId)
                .orElseThrow(() -> new RuntimeException("Worksheet not found"));
        workSheetRepo.delete(worksheet);
    }
}
