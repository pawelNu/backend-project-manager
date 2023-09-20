package com.pawelnu.backendprojectmanager.controller;

import com.pawelnu.backendprojectmanager.dto.UserDto;
import com.pawelnu.backendprojectmanager.entity.UserEntity;
import com.pawelnu.backendprojectmanager.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<UserEntity> getUserById(@PathVariable("id") UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping("")
    public String addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") UUID id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") UUID id,
                                @RequestBody UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

}
