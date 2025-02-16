package cvut.fit.cz.blog.controller.dto;

import cvut.fit.cz.blog.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserDto {

    private Long id_user;
    private String username;
    private String display_name;
    private String password;

    public UserDto(Long id_user, String username, String display_name, String password) {
        this.id_user = id_user;
        this.username = username;
        this.display_name = display_name;
        this.password = password;
    }

}
