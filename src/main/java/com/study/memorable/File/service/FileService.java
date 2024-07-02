package com.study.memorable.File.service;

import com.study.memorable.File.dto.FileCreateDTO;
import com.study.memorable.File.dto.FileReadDTO;
import com.study.memorable.File.entity.File;
import com.study.memorable.File.repo.FileRepo;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepo fileRepo;
    private final UserRepo userRepo;

    public void createFile(FileCreateDTO dto){
        File file = new File().toEntity(dto);
        User user = userRepo.findById(dto.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        file.setUser(user);
        fileRepo.save(file);
    }

    public List<FileReadDTO> findAll(){
        return fileRepo.findAll()
                .stream()
                .map(FileReadDTO::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id){
        fileRepo.deleteById(id);
    }

}
