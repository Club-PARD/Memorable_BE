package com.study.memorable.User.service;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void createUser(UserCreateDTO dto){
        userRepo.save(User.toEntity(dto));
    }

    public List<UserReadDTO> findAll() {
        return userRepo.findAll()
                .stream()
                .map(UserReadDTO::toDTO)
                .collect(Collectors.toList());
    }

    public UserReadDTO findUserById(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return UserReadDTO.toDTO(user);
    }

    public void updateUser(UserReadDTO dto, String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.updateFromDTO(dto);
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
    }
}
