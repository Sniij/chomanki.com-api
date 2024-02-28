package blog.sniij.api.dto;


import blog.sniij.domain.User;
import lombok.Getter;

public class UserDto {

    @Getter
    public static class Response{
        private String userId;

        private String nickname;

        private String imgUrl;

        private String email;

        private String provider;

        public Response(User user) {
            this.userId = user.getUserId();
            this.nickname = user.getNickname();
            this.imgUrl = user.getImgUrl();
            this.email = user.getEmail();
            this.provider = user.getProvider();
        }

        public Response(){}
    }
}
