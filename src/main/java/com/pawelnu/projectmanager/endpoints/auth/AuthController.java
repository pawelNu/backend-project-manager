package com.pawelnu.projectmanager.endpoints.auth;

import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.config.security.services.UserDetailsImpl;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRepository;
import com.pawelnu.projectmanager.exception.model.SimpleErrorResponse;
import com.pawelnu.projectmanager.utils.Path;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Path.API_AUTH)
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final EmployeeRepository employeeRepository;

  //  private final RoleRepository roleRepository;

  //  PasswordEncoder encoder;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    Authentication authentication;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));
    } catch (AuthenticationException exception) {
      Map<String, Object> map = new HashMap<>();
      map.put("message", "Bad credentials");
      map.put("status", false);
      return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwtString = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

    //    List<String> roles =
    //        userDetails.getAuthorities().stream()
    //            .map(item -> item.getAuthority())
    //            .collect(Collectors.toList());

    UserInfoResponse response =
        UserInfoResponse.builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .roles(null)
            .jwtToken(jwtString)
            .expireAt(jwtUtils.getExpirationDateFromToken(jwtString))
            .build();

    return ResponseEntity.ok(response);
  }

//  TODO fix register user
//  TODO fix Error: response status is 500 change to 401 or 403
  @PostMapping("/register-user")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (employeeRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new SimpleErrorResponse("Username is already taken!"));
    }

    if (employeeRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new SimpleErrorResponse("Email is already in use!"));
    }

    EmployeeEntity user =
        EmployeeEntity.builder()
            .firstName("test")
            .lastName("test")
            .username(signUpRequest.getUsername())
            .password(signUpRequest.getPassword())
            .email(signUpRequest.getEmail())
            .phoneNumber("test")
            .company(null)
            .build();

    //    Set<String> strRoles = signUpRequest.getRole();
    //    Set<Role> roles = new HashSet<>();

    //    if (strRoles == null) {
    //      Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
    //          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //      roles.add(userRole);
    //    } else {
    //      strRoles.forEach(role -> {
    //        switch (role) {
    //          case "admin":
    //            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
    //                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //            roles.add(adminRole);
    //
    //            break;
    //          case "seller":
    //            Role modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
    //                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //            roles.add(modRole);
    //
    //            break;
    //          default:
    //            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
    //                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    //            roles.add(userRole);
    //        }
    //      });
    //    }
    //
    //    user.setRoles(roles);
    employeeRepository.save(user);

    return ResponseEntity.ok(new SimpleErrorResponse("User registered successfully! OK!"));
  }

  @GetMapping("/username")
  public String currentUserName(Authentication authentication) {
    if (authentication != null) {
      return authentication.getName();
    } else {
      return "";
    }
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUserDetails(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    UserInfoResponse response =
        UserInfoResponse.builder()
            .id(userDetails.getId())
            .username(userDetails.getUsername())
            .roles(null)
            .jwtToken(null)
            .build();

    return ResponseEntity.ok().body(response);
  }
}
