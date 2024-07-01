package com.study.memorable.User.service;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import com.study.memorable.User.entity.User;
import com.study.memorable.User.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.metamodel.internal.MemberResolver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;

    public void createUser(UserCreateDTO dto){
        userRepo.save(new User().toEntity(dto));
    }

    public List<UserReadDTO> findAll(){
        return userRepo.findAll()
                .stream()
                .map(UserReadDTO::toDTO)
                .collect(Collectors.toList());

    }

    public UserReadDTO findUserById(Long id){
        return new UserReadDTO().toDTO(userRepo.findById(id).orElseThrow());
    }

    public void deleteById(Long id){
        userRepo.deleteById(id);
    }



}
