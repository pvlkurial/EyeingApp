package cvut.fit.cz.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id_blog;

    private String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "blog_post",
            joinColumns = @JoinColumn(name = "id_blog"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> posts = new HashSet<>();


    public Blog(String _title, User author) {
        this.title = _title;
        this.author = author;
    }

    public void addPost(Post post){
        posts.add((post));
    }
    public void removePost(Post post) {posts.remove(post);}
}
