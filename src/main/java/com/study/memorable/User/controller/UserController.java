package com.study.memorable.User.controller;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import com.study.memorable.User.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("")
    public String create(@RequestBody UserCreateDTO dto){
        userService.createUser(dto);
        return "유저 저장 성공";
    }

    @GetMapping("")
    public List<UserReadDTO> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDTO findUserById(@PathVariable Long id){
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id){
        userService.deleteById(id);
        return "유저 삭제됨.";
    }

}
