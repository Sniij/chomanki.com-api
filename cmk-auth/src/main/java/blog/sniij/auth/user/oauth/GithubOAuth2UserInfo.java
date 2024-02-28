package blog.sniij.auth.user.oauth;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(OAuth2Provider.GITHUB, attributes);
    }
    @Override
    public String getId() {
        return ( attributes.get("id")).toString();
    }

    @Override
    public String getNickname() {
        return attributes.get("login").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("login").toString().concat("@github.com");
    }

    @Override
    public String getImgUrl() {
        return attributes.get("avatar_url").toString();
    }
}
