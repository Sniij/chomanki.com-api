package blog.sniij.api;

import blog.sniij.api.dto.UserDto;
import blog.sniij.auth.token.jwt.JwtProvider;
import blog.sniij.domain.User;
import blog.sniij.dto.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@Controller
public class UserController {
    private final JwtProvider jwtProvider;
    public UserController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/user")
    public ResponseEntity<SingleResponseDto<UserDto.Response>> getUserProfile(
            @RequestHeader("Authorization") String authorization
    ) {

        User user = jwtProvider.getUserByRequestToken(authorization);


/*        if(Objects.isNull(authorization) ){
            return new ResponseEntity(new SingleResponseDto<>("Unauthorized user"),HttpStatus.UNAUTHORIZED);
        }else{
            String provider = userPrincipal.getUser().getProvider();
            OAuth2UserInfo oAuth2UserInfo;
            if(provider.equals("google")){
                oAuth2UserInfo = new GoogleOAuth2UserInfo(userPrincipal.getAttributes());
                oAuth2UserInfo.setUserId(userPrincipal.getUser().getUserId());
            }
            else {
                oAuth2UserInfo = new GithubOAuth2UserInfo(userPrincipal.getAttributes());
                oAuth2UserInfo.setUserId(userPrincipal.getUser().getUserId());
            }*/

            return new ResponseEntity<>(new SingleResponseDto<>(new UserDto.Response(user)), HttpStatus.OK);
    }



    @GetMapping("/user/test")
    public void test(
            @RequestHeader("Authorization") String authorization
    ) {
        //String userId = tokenResolver.getUserId(authorization);

        User user = jwtProvider.getUserByRequestToken(authorization);

        System.out.println(user.getNickname());
/*        if(Objects.isNull(authorization) ){
            return new ResponseEntity(new SingleResponseDto<>("Unauthorized user"),HttpStatus.UNAUTHORIZED);
        }else{
            String provider = userPrincipal.getUser().getProvider();
            OAuth2UserInfo oAuth2UserInfo;
            if(provider.equals("google")){
                oAuth2UserInfo = new GoogleOAuth2UserInfo(userPrincipal.getAttributes());
                oAuth2UserInfo.setUserId(userPrincipal.getUser().getUserId());
            }
            else {
                oAuth2UserInfo = new GithubOAuth2UserInfo(userPrincipal.getAttributes());
                oAuth2UserInfo.setUserId(userPrincipal.getUser().getUserId());
            }*/
    }
}
