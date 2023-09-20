package com.pawelnu.backendprojectmanager.service;

import com.pawelnu.backendprojectmanager.dto.UserDto;
import com.pawelnu.backendprojectmanager.entity.UserEntity;
import com.pawelnu.backendprojectmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public String addUser(UserDto userDto) {
        UserEntity newUser = new UserEntity();

        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail());

        userRepository.save(newUser);
        return "User with email: " + userDto.getEmail() + " was added.";
    }

    public String deleteUser(UUID id) {
        userRepository.deleteById(id);
        return "User was deleted.";
    }

    public String updateUser(UUID id, UserDto userDto) {
        UserEntity userToEdit = userRepository.getReferenceById(id);

        userToEdit.setFirstName(userDto.getFirstName());
        userToEdit.setLastName(userDto.getLastName());
        userToEdit.setEmail(userDto.getEmail());

        userRepository.save(userToEdit);
        return "User with email: " + userDto.getEmail() + " was updated.";
    }
}
