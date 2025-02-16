package cvut.fit.cz.blog.controller;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.UserDto;
import cvut.fit.cz.blog.service.BlogService;
import cvut.fit.cz.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for user management and profile retrieval")
public class UserController {

    private final UserService userService;
    private final BlogService blogService;

    public UserController(UserService userService, BlogService blogService) {
        this.userService = userService;
        this.blogService = blogService;
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping
    public Iterable<UserDto> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Get a user by ID", description = "Retrieves a specific user by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/id/{id}")
    public UserDto getUserById(
            @Parameter(description = "ID of the user to retrieve")
            @PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Delete a user by ID", description = "Deletes a specific user by their unique ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(
            @Parameter(description = "ID of the user to delete")
            @PathVariable("id") Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Operation(summary = "Get blogs of a user by ID", description = "Retrieves all blogs associated with a specific user ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/id/{id}/blogs")
    public Iterable<BlogDto> getAllUsersBlogs(
            @Parameter(description = "ID of the user whose blogs are retrieved")
            @PathVariable("id") Long id) {
        return blogService.getBlogsByUserId(id);
    }

    @Operation(summary = "Delete all users", description = "Removes all users from the system.")
    @ApiResponse(responseCode = "200", description = "All users deleted successfully")
    @PostMapping("/deleteall")
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @Operation(summary = "Get current authenticated user", description = "Retrieves the profile of the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDto userEntity = userService.findByUsername(currentUser.getUsername());
        if (userEntity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userEntity);
    }

    @Operation(summary = "Get current authenticated user by principal", description = "Retrieves the current authenticated user using the Principal object.")
    @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    @GetMapping("/current-user")
    public UserDto getCurrentUser(Principal principal) {
        return userService.findByUsername(principal.getName());
    }

    @Operation(summary = "Get a user by username", description = "Retrieves a specific user by their username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/name/{username}")
    public UserDto getUserByUsername(
            @Parameter(description = "Username of the user to retrieve")
            @PathVariable("username") String username) {
        return userService.findByUsername(username);
    }

    @Operation(summary = "Get blogs of a user by username", description = "Retrieves all blogs associated with a specific username.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/name/{username}/blogs")
    public Iterable<BlogDto> getAllUsersBlogsByUsername(
            @Parameter(description = "Username of the user whose blogs are retrieved")
            @PathVariable("username") String username) {
        return blogService.getBlogsByUsername(username);
    }
}
