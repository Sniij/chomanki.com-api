package blog.sniij.auth.user.oauth;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(OAuth2Provider.GOOGLE, attributes);
    }
    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImgUrl() {
        return (String) attributes.get("picture");
    }


}
