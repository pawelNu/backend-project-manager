package com.pawelnu.projectmanager.config.security.services;

import com.pawelnu.projectmanager.endpoints.employee.EmployeeMapper;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRowDTO;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

  private final EmployeeService employeeService;
  private final EmployeeMapper employeeMapper;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    EmployeeRowDTO employeeRowDTO = employeeService.findByUsernameWithAuthorities(username);
    UserDetailsDTO userDetails = employeeMapper.toUserDetailsDTO(employeeRowDTO);
    return UserDetailsImpl.build(userDetails);
  }
}
