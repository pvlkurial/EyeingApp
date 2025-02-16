package cvut.fit.cz.blog.controller;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.PostDto;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.UserRepository;
import cvut.fit.cz.blog.service.BlogService;
import cvut.fit.cz.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Endpoints for managing posts and their associations with blogs")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final BlogService blogService;

    public PostController(PostService postService, UserRepository userRepository, BlogService blogService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.blogService = blogService;
    }

    @Operation(summary = "Create a new post", description = "Creates a post and assigns it to the authenticated user and a blog.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Failed to get user data")
    })
    @PostMapping("/create/{id_blog}")
    public String create(
            @RequestBody PostDto postDto,
            @Parameter(description = "ID of the blog where the post will be created")
            @PathVariable("id_blog") Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return "Failed to get user data";
        }
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        postDto.setAuthor(user.get().getId_user());
        postService.saveWithBlog(postDto, id);
        return "Successfully Created Post!";
    }

    @Operation(summary = "Get all posts", description = "Retrieves all posts from the system.")
    @ApiResponse(responseCode = "200", description = "Posts retrieved successfully")
    @GetMapping
    public Iterable<PostDto> getAll() {
        return postService.getAll();
    }

    @Operation(summary = "Find a post by ID", description = "Retrieves a specific post by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id_post}")
    public PostDto findPostById(
            @Parameter(description = "ID of the post to retrieve")
            @PathVariable("id_post") Long id) {
        return postService.findById(id);
    }

    @Operation(summary = "Delete a post by ID", description = "Deletes a specific post based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "500", description = "Error deleting post")
    })
    @PostMapping("/{id_post}/delete")
    public ResponseEntity<String> deleteById(
            @Parameter(description = "ID of the post to delete")
            @PathVariable("id_post") Long id) {

        if (postService.deleteById(id)) {
            return ResponseEntity.ok("Post Deleted Successfully");
        }
        return ResponseEntity.status(500).body("Error deleting post");
    }

    @Operation(summary = "Delete all posts", description = "Removes all posts from the system.")
    @ApiResponse(responseCode = "200", description = "All posts deleted successfully")
    @PostMapping("/deleteall")
    public void deleteAllPosts() {
        postService.deleteAllPosts();
    }

    @Operation(summary = "Get blogs of a specific post", description = "Finds all blogs that contain a given post.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{id_post}/blogs")
    public Iterable<BlogDto> findBlogsOfPost(
            @Parameter(description = "ID of the post to retrieve blogs for")
            @PathVariable("id_post") Long id) {
        return blogService.getAllBlogsByPostId(id);
    }

    @Operation(summary = "Add a post to a blog", description = "Associates an existing post with a specific blog.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post added to blog successfully"),
            @ApiResponse(responseCode = "404", description = "Blog or post not found")
    })
    @PostMapping("/{id_post}/addto/{id_blog}")
    public ResponseEntity<String> addPostToBlog(
            @Parameter(description = "ID of the post to add")
            @PathVariable("id_post") Long id_post,
            @Parameter(description = "ID of the blog to add the post to")
            @PathVariable("id_blog") Long id_blog) {

        return postService.addPostToBlog(id_post, id_blog);
    }
}
