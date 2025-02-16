package cvut.fit.cz.blog.repository;

import cvut.fit.cz.blog.domain.Blog;
import cvut.fit.cz.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Iterable<Blog> findByAuthor(User user);
    @Query("SELECT DISTINCT b FROM Blog b JOIN b.posts p ORDER BY b.title ASC")
    List<Blog> findBlogsWithPosts();

}
