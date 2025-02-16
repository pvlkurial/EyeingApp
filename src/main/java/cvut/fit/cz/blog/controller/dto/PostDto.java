package cvut.fit.cz.blog.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PostDto {

    private Long id_post;
    private String title;
    private String description;
    private Long author;

    public PostDto(Long id_post, String title, String description, Long author) {
        this.id_post = id_post;
        this.title = title;
        this.description = description;
        this.author = author;
    }

}
