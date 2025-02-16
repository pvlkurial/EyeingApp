package cvut.fit.cz.blog.repository;
import cvut.fit.cz.blog.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long>{
}
