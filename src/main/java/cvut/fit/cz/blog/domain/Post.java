package cvut.fit.cz.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long post_id;

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @ManyToMany(mappedBy = "posts")
    private Set<Blog> blogsOfPost = new HashSet<>();




    public Post(String title, String description, User author){
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public void addThisPostToBlog(Blog blog){
        blogsOfPost.add(blog);
    }
    public void removeThisPostFromBlog(Blog blog) {blogsOfPost.remove(blog);}
}
