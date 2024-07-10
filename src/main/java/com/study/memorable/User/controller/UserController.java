package com.study.memorable.User.controller;

import com.study.memorable.User.dto.UserCreateDTO;
import com.study.memorable.User.dto.UserReadDTO;
import com.study.memorable.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return "Success";
    }

    @GetMapping("")
    public List<UserReadDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserReadDTO findUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
