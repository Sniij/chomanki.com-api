package blog.sniij.auth.user.oauth;

import java.util.Map;

public abstract class OAuth2UserInfo {
    private String userId;

    protected Map<String, Object> attributes;
    protected OAuth2Provider provider;
    public OAuth2UserInfo(OAuth2Provider provider, Map<String, Object> attributes) {
        this.provider = provider;
        this.attributes = attributes;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getEmail();

    public abstract String getImgUrl();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
