package blog.sniij.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("RefreshToken")
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    private LocalDateTime createdAt;


    @Builder
    public RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }

}
