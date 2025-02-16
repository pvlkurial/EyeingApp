package cvut.fit.cz.blog;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.domain.Blog;
import cvut.fit.cz.blog.domain.Post;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.BlogRepository;
import cvut.fit.cz.blog.repository.PostRepository;
import cvut.fit.cz.blog.repository.UserRepository;
import cvut.fit.cz.blog.service.BlogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BlogServiceIntegrationTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Blog testBlog;
    private Post testPost;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        testBlog = new Blog();
        testBlog.setTitle("Test Blog");
        testBlog.setAuthor(testUser);
        testBlog = blogRepository.save(testBlog);

        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setDescription("This is a test post");
        testPost = postRepository.save(testPost);
    }

    @Test
    @Transactional
    void testAddNewPostToBlog() {
        blogService.addNewPostToBlog(testBlog.getId_blog(), testPost.getPost_id());

        Optional<Blog> updatedBlog = blogRepository.findById(testBlog.getId_blog());
        Optional<Post> updatedPost = postRepository.findById(testPost.getPost_id());

        Assertions.assertNotNull(updatedBlog);
        Assertions.assertNotNull(updatedPost);
        Assertions.assertTrue(updatedBlog.get().getPosts().contains(testPost));
    }

    @Test
    @Transactional
    void testGetBlogsByUsername() {
        List<BlogDto> blogs = (List<BlogDto>) blogService.getBlogsByUsername("testuser");

        Assertions.assertNotNull(blogs);
        Assertions.assertEquals(1, blogs.size());
        Assertions.assertEquals("Test Blog", blogs.get(0).getTitle());
        Assertions.assertEquals(testUser.getId_user(), blogs.get(0).getAuthor_id());

    }
}
