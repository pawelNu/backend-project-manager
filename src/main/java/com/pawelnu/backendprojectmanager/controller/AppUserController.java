package com.pawelnu.backendprojectmanager.controller;

import com.pawelnu.backendprojectmanager.dto.AppUserDto;
import com.pawelnu.backendprojectmanager.entity.AppUserEntity;
import com.pawelnu.backendprojectmanager.service.AppUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/app-users")
@Tag(name = "AppUserController")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("")
    public List<AppUserEntity> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<AppUserEntity> getUserById(@PathVariable("id") UUID id) {
        return appUserService.getUserById(id);
    }

    @PostMapping("")
    public String addUser(@RequestBody AppUserDto appUserDto) {
        return appUserService.addUser(appUserDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") UUID id) {
        return appUserService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") UUID id,
                                @RequestBody AppUserDto appUserDto) {
        return appUserService.updateUser(id, appUserDto);
    }

}
