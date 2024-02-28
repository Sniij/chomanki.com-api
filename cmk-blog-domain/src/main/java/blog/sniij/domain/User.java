package blog.sniij.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("User")
@NoArgsConstructor
@Getter
public class User {

    @Id
    private String userId;

    private String nickname;

    private String imgUrl;

    private String email;

    private String provider;

    private List<String > roles = new ArrayList<>();
    @Builder
    public User(String userId, String nickname, String imgUrl,String email,String provider, List<String> roles) {
        this.userId = userId;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.email = email;
        this.provider = provider;
        this.roles = roles;
    }

}
