package cvut.fit.cz.blog;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.UserDto;
import cvut.fit.cz.blog.domain.Blog;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.BlogRepository;
import cvut.fit.cz.blog.repository.PostRepository;
import cvut.fit.cz.blog.repository.UserRepository;
import cvut.fit.cz.blog.service.BlogService;
import cvut.fit.cz.blog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BlogServiceUnitTests {
    @MockBean
    private BlogRepository blogRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    private Blog blog;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId_user(1L);

        blog = new Blog();
        blog.setId_blog(1L);
        blog.setTitle("Test blog");
        blog.setAuthor(user);
    }

    @Test
    void testSaveBlog() {
        BlogDto blogDto = new BlogDto(1L, "Test blog", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        BlogDto savedBlog = blogService.save(blogDto);

        assertNotNull(savedBlog);
        assertEquals("Test blog", savedBlog.getTitle());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    void testGetAllBlogs() {
        when(blogRepository.findAll()).thenReturn(Arrays.asList(blog));

        Iterable<BlogDto> blogs = blogService.getAll();
        List<BlogDto> blogList = (List<BlogDto>) blogs;

        assertFalse(blogList.isEmpty());
        assertEquals(1, blogList.size());
        verify(blogRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        blogRepository.save(new Blog("new blog", new User()));
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        BlogDto foundBlog = blogService.findById(1L);

        assertNotNull(foundBlog);
        assertEquals(1L, foundBlog.getId_blog());
        verify(blogRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));

        boolean deleted = blogService.deleteById(1L);

        assertTrue(deleted);
        verify(blogRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateBlog() {
        BlogDto blogDto = new BlogDto(1L, "Updated Blog", 1L);
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        BlogDto updatedBlog = blogService.update(1L, blogDto);

        assertEquals("Updated Blog", updatedBlog.getTitle());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

    @Test
    void testSaveBlogException() {
        BlogDto blogDto = new BlogDto(1L, "Exception Blog", 5L);
        assertThrows(IllegalArgumentException.class, () -> {
            blogService.save(blogDto);
        });
    }

    // Test for updating a non-existent user
    @Test
    void testSaveUserException() {
        UserDto userDto = new UserDto(10L, "Exception User", "EU", "pswrd");
        assertThrows(RuntimeException.class, () -> {
            userService.update(10L, userDto);
        });
    }



}

