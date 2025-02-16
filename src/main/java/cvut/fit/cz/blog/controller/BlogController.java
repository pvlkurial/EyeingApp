package cvut.fit.cz.blog.controller;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.PostDto;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.UserRepository;
import cvut.fit.cz.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/blogs")
@Tag(name = "Blogs", description = "Endpoints for managing blogs and related posts")
public class BlogController {

    private final BlogService blogService;
    private final UserRepository userRepository;

    public BlogController(BlogService blogService, UserRepository userRepository) {
        this.blogService = blogService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get all blogs", description = "Retrieves a list of all available blogs.")
    @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully")
    @GetMapping()
    public Iterable<BlogDto> getAllBlogs() {
        return this.blogService.getAll();
    }

    @Operation(summary = "Get all blogs with posts", description = "Retrieves all blogs along with their associated posts.")
    @ApiResponse(responseCode = "200", description = "Blogs with posts retrieved successfully")
    @GetMapping("/withposts")
    public Iterable<BlogDto> getAllBlogsWithPosts() {
        return this.blogService.getAllBlogsWithPosts();
    }

    @Operation(summary = "Create a new blog", description = "Creates a new blog and assigns it to the authenticated user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog created successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/create")
    public String create(@RequestBody BlogDto blogDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        try {
            if (user.isPresent()) {
                blogDto.setAuthor_id(user.get().getId_user());
                blogService.save(blogDto);
            }
            return "Success";
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404));
        }
    }

    @Operation(summary = "Get a blog by ID", description = "Retrieves a specific blog by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Blog not found")
    })
    @GetMapping("/{id}")
    public BlogDto getBlogById(
            @Parameter(description = "ID of the blog to retrieve")
            @PathVariable("id") Long id) {
        return blogService.findById(id);
    }

    @Operation(summary = "Delete all blogs", description = "Removes all blogs from the system.")
    @ApiResponse(responseCode = "200", description = "All blogs deleted successfully")
    @PostMapping("/deleteall")
    public void deleteAllBlogs() {
        blogService.deleteAll();
    }

    @Operation(summary = "Delete a blog by ID", description = "Removes a specific blog by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Blog not found")
    })
    @PostMapping("/{id}/delete")
    public void deleteBlogById(
            @Parameter(description = "ID of the blog to delete")
            @PathVariable("id") Long id) {
        blogService.deleteById(id);
    }

    @Operation(summary = "Get posts for a specific blog", description = "Retrieves all posts associated with a given blog.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Posts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Blog not found")
    })
    @GetMapping("/{id}/posts")
    public Iterable<PostDto> getPostsByBlog(
            @Parameter(description = "ID of the blog to retrieve posts for")
            @PathVariable("id") Long id) {
        return blogService.getAllPostsByBlogId(id);
    }

    @Operation(summary = "Add a post to a blog", description = "Associates an existing post with a specific blog.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post added to blog successfully"),
            @ApiResponse(responseCode = "404", description = "Blog or post not found")
    })
    @PostMapping("/{blog_id}/posts/{post_id}")
    public void addNewPostToBlog(
            @Parameter(description = "ID of the blog to add the post to")
            @PathVariable("blog_id") Long blog_id,
            @Parameter(description = "ID of the post to add")
            @PathVariable("post_id") Long post_id) {
        blogService.addNewPostToBlog(blog_id, post_id);
    }
}
