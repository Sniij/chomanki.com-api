package blog.sniij.auth.user.oauth;

import blog.sniij.exception.BusinessLogicException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Map;

public interface OAuth2UserInfoFactory {

    static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {

        OAuth2UserInfo userInfo = null;
        OAuth2Provider oAuth2Provider = OAuth2Provider.convert(registrationId);
        switch (oAuth2Provider) {
            case GOOGLE:
                userInfo = new GoogleOAuth2UserInfo(attributes);
                break;
            case GITHUB:
                userInfo = new GithubOAuth2UserInfo(attributes);
                break;
            default:
                throw new BusinessLogicException(HttpStatus.BAD_REQUEST, "Not Supported provider");
        }

        return userInfo;
    }

    static OAuth2UserInfo getOAuth2UserInfo(OAuth2AuthenticationToken auth2AuthenticationToken) {
        return OAuth2UserInfoFactory.getOAuth2UserInfo(
                auth2AuthenticationToken.getAuthorizedClientRegistrationId(),
                auth2AuthenticationToken.getPrincipal().getAttributes()
        );
    }
}
