package com.pawelnu.projectmanager.endpoints.auth;

import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.config.security.services.UserDetailsImpl;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeRepository;
import com.pawelnu.projectmanager.exception.model.SimpleErrorResponse;
import jakarta.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final EmployeeRepository employeeRepository;
  private final RoleRepository roleRepository;

  PasswordEncoder encoder;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
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

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    UserInfoResponse response =
        new UserInfoResponse(
            userDetails.getId(), userDetails.getUsername(), roles, jwtCookie.toString());

    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (employeeRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new SimpleErrorResponse("Username is already taken!"));
    }

    if (employeeRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new SimpleErrorResponse("Email is already in use!"));
    }

    // Create new user's account
    EmployeeEntity user =
        new EmployeeEntity(
            signUpRequest.getUsername(),
            signUpRequest.getEmail(),
            encoder.encode(signUpRequest.getPassword()));

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
    if (authentication != null) return authentication.getName();
    else return "";
  }

  @GetMapping("/user")
  public ResponseEntity<?> getUserDetails(Authentication authentication) {
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    UserInfoResponse response =
        new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);

    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/signout")
  public ResponseEntity<?> signoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
