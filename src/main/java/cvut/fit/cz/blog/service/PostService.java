package cvut.fit.cz.blog.service;

import cvut.fit.cz.blog.controller.dto.PostDto;
import cvut.fit.cz.blog.domain.Blog;
import cvut.fit.cz.blog.domain.Post;
import cvut.fit.cz.blog.repository.BlogRepository;
import cvut.fit.cz.blog.repository.PostRepository;
import cvut.fit.cz.blog.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements CrudService<PostDto, Long>{

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;


    public PostService(UserRepository userRepository, PostRepository postRepository, BlogRepository blogRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.blogRepository = blogRepository;
    }

    @Override
    public PostDto save(PostDto postDto) {
        Post post = new Post(postDto.getTitle(), postDto.getDescription(),
                userRepository.findById(postDto.getAuthor()).orElseThrow(IllegalArgumentException::new));

        Post newPost = postRepository.save(post);
        return new PostDto(newPost.getPost_id(), newPost.getTitle(),
                           newPost.getDescription(), newPost.getAuthor().getId_user());
    }

    @Override
    public Iterable<PostDto> getAll() {
        Iterable<Post> posts = postRepository.findAll();
        List<PostDto> returnPosts = new ArrayList<>();

        for(Post post : posts){
            if(post.getAuthor() == null){
                returnPosts.add(new PostDto(post.getPost_id(), post.getTitle(), post.getDescription(), 0L));
            }else{
                returnPosts.add(new PostDto(post.getPost_id(), post.getTitle(), post.getDescription(), post.getAuthor().getId_user()));
            }

        }
        return returnPosts;
    }

    @Override
    public PostDto findById(Long aLong) {
        Optional<Post> post = postRepository.findById(aLong);
        return new PostDto(post.get().getPost_id(), post.get().getTitle(), post.get().getDescription(), post.get().getAuthor().getId_user());
    }

    @Override
    public boolean deleteById(Long aLong) {
        Optional<Post> post = postRepository.findById(aLong);

        if(post.isPresent()){
            for(Blog blog : post.get().getBlogsOfPost()){
                blog.removePost(post.get());
                blogRepository.save(blog);
            }
            postRepository.deleteById(aLong);
            return true;
        }
        return false;
    }

    @Override
    public PostDto update(Long aLong, PostDto postDto) {
        Post post = postRepository.findById(aLong).orElseThrow(RuntimeException::new);

        post.setAuthor(userRepository.findById(postDto.getAuthor()).orElseThrow(RuntimeException::new));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        postRepository.save(post);
        return new PostDto(post.getPost_id(), post.getTitle(), post.getDescription(), post.getAuthor().getId_user());
    }

    @Override
    public PostDto findByUsername(String username) {
        return null;
    }

    public Iterable<PostDto> getAllPostsByBlogId(Long id){
        Optional<Blog> blog = blogRepository.findById(id);
        List<PostDto> returnPosts = new ArrayList<>();
        for(Post post : blog.get().getPosts()){
            returnPosts.add(new PostDto(post.getPost_id(), post.getTitle(), post.getDescription(), post.getAuthor().getId_user()));
        }
        return returnPosts;
    }

    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    public PostDto saveWithBlog(PostDto postDto, Long id){
        Post post = new Post(postDto.getTitle(), postDto.getDescription(),
                userRepository.findById(postDto.getAuthor()).orElseThrow(IllegalArgumentException::new));

        blogRepository.findById(id).get().addPost(post);
        Post newPost = postRepository.save(post);
        newPost.addThisPostToBlog(blogRepository.findById(id).get());

        return new PostDto(newPost.getPost_id(), newPost.getTitle(),
                newPost.getDescription(), newPost.getAuthor().getId_user());
    }
    public ResponseEntity<String> addPostToBlog(Long post_id, Long blog_id){
        System.out.println("CHECK ONE");
        Optional<Blog> blog = blogRepository.findById(blog_id);
        if(blog.isPresent()){
            System.out.println("CHECK TWO");

            Optional<Post> post = postRepository.findById(post_id);
            if(post.isPresent()){
                System.out.println("CHECK THREE");
                System.out.println("Blog: " + blog.get().getTitle() + " Post: " + post.get().getTitle());
                blog.get().addPost(post.get());
                blogRepository.save(blog.get());
                return ResponseEntity.ok("Added succesfully");
            }
        }
        return ResponseEntity.status(500).body("Post not added!");
    }

}
