package cvut.fit.cz.blog.controller.dto;

import cvut.fit.cz.blog.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BlogDto {
    private Long id_blog;
    private String title;
    private Long author_id;

    public BlogDto(Long id_blog, String _title, Long author_id) {
        this.id_blog = id_blog;
        this.title = _title;
        this.author_id = author_id;
    }
}