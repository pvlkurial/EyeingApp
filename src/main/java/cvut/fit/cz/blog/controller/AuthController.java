package cvut.fit.cz.blog.controller;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.LoginRequest;
import cvut.fit.cz.blog.controller.dto.UserDto;
import cvut.fit.cz.blog.domain.AuthResponse;
import cvut.fit.cz.blog.service.AuthService;
import cvut.fit.cz.blog.service.BlogService;
import cvut.fit.cz.blog.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for authentication and user-related actions")
public class AuthController {

    private final AuthService authService;
    private final BlogService blogService;

    public AuthController(AuthService authService, BlogService blogService) {
        this.authService = authService;
        this.blogService = blogService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a success message.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto user) {
        return authService.register(user);
    }

    @Operation(summary = "Get currently logged-in user", description = "Retrieves details of the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User data retrieved"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/current-user")
    public UserDto getCurrentUser(
            @Parameter(description = "Authenticated user details")
            @AuthenticationPrincipal CustomUserDetailsService u) {
        return authService.getCurrentUser(u);
    }

    @Operation(summary = "Get blogs of the current user", description = "Fetches all blogs created by the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blogs retrieved"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping("/current-user/blogs")
    public Iterable<BlogDto> getCurrentUsersBlogs(
            @Parameter(description = "Authenticated user details")
            @AuthenticationPrincipal CustomUserDetailsService u) {
        return blogService.getBlogsByUserId(authService.getCurrentUser(u).getId_user());
    }

    @Operation(summary = "User login", description = "Authenticates the user and returns a token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "User logout", description = "Logs out the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authService.logout();
    }
}
