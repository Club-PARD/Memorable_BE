package com.study.memorable.User.controller;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import com.study.memorable.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    public String create(@RequestBody UserCreateDTO dto) {
        userService.createUser(dto);
        return "유저 생성됨!";
    }

    @GetMapping("")
    public List<UserReadDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDTO findUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @PatchMapping("/{id}")
    public void updateUser(@RequestBody UserReadDTO dto, @PathVariable String id){
        userService.updateUser(dto, id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "유저 삭제됨!";
    }
}
