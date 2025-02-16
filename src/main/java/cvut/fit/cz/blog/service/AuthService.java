package cvut.fit.cz.blog.service;

import cvut.fit.cz.blog.config.JwtUtils;
import cvut.fit.cz.blog.controller.dto.LoginRequest;
import cvut.fit.cz.blog.controller.dto.UserDto;
import cvut.fit.cz.blog.domain.AuthResponse;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<String> register(UserDto user){

        Optional<User> found = userRepository.findByUsername(user.getUsername());
            if (found.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: User Already Exists!");
        }
        User entity = new User(user.getUsername(), user.getDisplay_name(), user.getPassword());
            userRepository.save(entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    public UserDto getCurrentUser(CustomUserDetailsService u){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null){
            return null;
        }
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        return new UserDto(user.get().getId_user(), user.get().getUsername(), user.get().getDisplayname(), user.get().getPassword());
    }

    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AuthResponse(jwtUtils.generateToken(authentication.getName())));
    }

    public ResponseEntity<String> logout(){
        return ResponseEntity.ok("Logged out succesfully!");
    }



}
