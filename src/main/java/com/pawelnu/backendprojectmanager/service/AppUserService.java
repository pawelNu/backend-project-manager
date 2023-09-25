package com.pawelnu.backendprojectmanager.service;

import com.pawelnu.backendprojectmanager.dto.AppUserDto;
import com.pawelnu.backendprojectmanager.entity.AppUserEntity;
import com.pawelnu.backendprojectmanager.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    public List<AppUserEntity> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUserEntity> getUserById(UUID id) {
        return appUserRepository.findById(id);
    }

    public String addUser(AppUserDto appUserDto) {
        AppUserEntity newUser = new AppUserEntity();

        newUser.setFirstName(appUserDto.getFirstName());
        newUser.setLastName(appUserDto.getLastName());
        newUser.setEmail(appUserDto.getEmail());

        appUserRepository.save(newUser);
        return "User with email: " + appUserDto.getEmail() + " was added.";
    }

    public String deleteUser(UUID id) {
        appUserRepository.deleteById(id);
        return "User was deleted.";
    }

    public String updateUser(UUID id, AppUserDto appUserDto) {
        AppUserEntity userToEdit = appUserRepository.getReferenceById(id);

        userToEdit.setFirstName(appUserDto.getFirstName());
        userToEdit.setLastName(appUserDto.getLastName());
        userToEdit.setEmail(appUserDto.getEmail());

        appUserRepository.save(userToEdit);
        return "User with email: " + appUserDto.getEmail() + " was updated.";
    }
}
