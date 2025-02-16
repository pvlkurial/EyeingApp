package cvut.fit.cz.blog.service;

import cvut.fit.cz.blog.controller.dto.BlogDto;
import cvut.fit.cz.blog.controller.dto.PostDto;
import cvut.fit.cz.blog.domain.Blog;
import cvut.fit.cz.blog.domain.Post;
import cvut.fit.cz.blog.domain.User;
import cvut.fit.cz.blog.repository.BlogRepository;
import cvut.fit.cz.blog.repository.PostRepository;
import cvut.fit.cz.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BlogService implements CrudService<BlogDto, Long>{
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public BlogService(BlogRepository blogRepository, UserRepository userRepository, PostRepository postRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }



    @Override
    public BlogDto save(BlogDto blogDto) {
        Blog post = new Blog(blogDto.getTitle(), userRepository.findById(blogDto.getAuthor_id()).orElseThrow(() -> new IllegalArgumentException()));
        Blog newBlog = blogRepository.save(post);
        return new BlogDto(newBlog.getId_blog(), newBlog.getTitle(), newBlog.getAuthor().getId_user());
    }



    @Override
    public Iterable<BlogDto> getAll() {
        Iterable<Blog> blogs = blogRepository.findAll();
        List<BlogDto> returnBlogs = new ArrayList<>();

        for(Blog blog : blogs){
            if(blog.getAuthor() != null){
                returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getAuthor().getId_user()));
            }else{
                returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), 0L));
            }
        }
        return returnBlogs;
    }

    @Override
    public BlogDto findById(Long aLong) {
        Optional<Blog> blog = blogRepository.findById(aLong);
        return new BlogDto(blog.get().getId_blog(), blog.get().getTitle(), blog.get().getAuthor().getId_user());
    }

    @Override
    public boolean deleteById(Long aLong) {
        Optional<Blog> blog = blogRepository.findById(aLong);
        if(blog.isPresent()){
            blogRepository.deleteById(aLong);
            return true;
        }
        return false;
    }

    @Override
    public BlogDto update(Long aLong, BlogDto blogDto) {
        Blog blog = blogRepository.findById(aLong).orElseThrow(RuntimeException::new);

        blog.setAuthor(userRepository.findById(blogDto.getAuthor_id()).orElseThrow(RuntimeException::new));
        blog.setTitle(blogDto.getTitle());
        blogRepository.save(blog);
        return new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getAuthor().getId_user());
    }

    @Override
    public BlogDto findByUsername(String username) {
        return null;
    }

    public void deleteAll(){
        blogRepository.deleteAll();
    }

    public Iterable<BlogDto> getBlogsByUserId(Long id){
        Iterable<Blog> blogs = blogRepository.findAll();
        List<BlogDto> returnBlogs = new ArrayList<>();

        for(Blog blog : blogs){
            if(blog.getAuthor() != null && Objects.equals(blog.getAuthor().getId_user(), id)){
                returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getAuthor().getId_user()));
            }
        }
        return returnBlogs;
    }

    public void addNewPostToBlog(Long blog_id, Long post_id){
        Blog blog = blogRepository.findById(blog_id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Post post = postRepository.findById(post_id).orElseThrow(() -> new IllegalArgumentException("Post not found"));

        blog.addPost(post);
        post.addThisPostToBlog(blog);

        blogRepository.save(blog);
        postRepository.save(post);
    }
    public Iterable<BlogDto> getBlogsByUsername(String username){
        List<BlogDto> returnBlogs = new ArrayList<>();
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            return null;
        }
        Iterable<Blog> blogs = blogRepository.findByAuthor(user.get());
        for(Blog blog : blogs){
            returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getAuthor().getId_user()));
        }
        if(returnBlogs.isEmpty()){
            return null;
        }
        return returnBlogs;
    }

    public Iterable<PostDto> getAllPostsByBlogId(Long id){
        List<PostDto> returnPosts = new ArrayList<>();

        Iterable<Post> posts = blogRepository.findById(id).get().getPosts();

        for(Post post : posts){
            System.out.println(post.getTitle());
            returnPosts.add(new PostDto(post.getPost_id(), post.getTitle(), post.getDescription(), post.getAuthor().getId_user()));
        }
        return returnPosts;
    }
    public Iterable<BlogDto> getAllBlogsByPostId(Long id){
        List<BlogDto> returnBlogs = new ArrayList<>();
        Iterable<Blog> blogs = postRepository.findById(id).get().getBlogsOfPost();

        for(Blog blog : blogs){
            returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getId_blog()));
        }
        return returnBlogs;
    }

    public Iterable<BlogDto> getAllBlogsWithPosts(){
        Iterable<Blog> blogs = blogRepository.findBlogsWithPosts();
        List<BlogDto> returnBlogs = new ArrayList<>();

        for(Blog blog : blogs){
            if(blog.getAuthor() != null){
                returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), blog.getAuthor().getId_user()));
            }else{
                returnBlogs.add(new BlogDto(blog.getId_blog(), blog.getTitle(), 0L));
            }
        }
        return returnBlogs;
    }

}

