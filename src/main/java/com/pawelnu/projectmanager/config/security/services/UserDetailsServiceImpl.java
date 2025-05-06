package com.pawelnu.projectmanager.config.security.services;

import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRepository;
import com.pawelnu.projectmanager.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final EmployeeRepository employeeRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    EmployeeEntity user =
        employeeRepository
            .findByUsername(username)
            .orElseThrow(() -> new NotFoundException("User not found with username: " + username));

    return UserDetailsImpl.build(user);
  }
}
