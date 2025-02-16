package cvut.fit.cz.blog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userTable")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_user;

    private String username;
    private String displayname;
    private String password;

    public User(String username, String displayname, String password){
        this.username = username;
        this.displayname = displayname;
        this.password = password;
    }

}
