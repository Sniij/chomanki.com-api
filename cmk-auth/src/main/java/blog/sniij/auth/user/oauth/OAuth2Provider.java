package blog.sniij.auth.user.oauth;

import java.util.Arrays;

public enum OAuth2Provider {
    GOOGLE,
    GITHUB;

    public static OAuth2Provider convert(String registrationId) {
        return Arrays.stream(OAuth2Provider.values())
                .filter(provider -> provider.toString().equals(registrationId.toUpperCase()))
                .findAny()
                .orElseThrow();
    }


}
