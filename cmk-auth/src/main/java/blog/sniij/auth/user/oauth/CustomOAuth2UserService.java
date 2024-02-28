package blog.sniij.auth.user.oauth;


import blog.sniij.auth.token.util.CustomAuthorityUtils;
import blog.sniij.domain.User;
import blog.sniij.exception.BusinessLogicException;
import blog.sniij.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserService userService;
    private final CustomAuthorityUtils authorityUtils;

    public CustomOAuth2UserService(UserService userService, CustomAuthorityUtils authorityUtils) {
        this.userService = userService;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User defaultOAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                provider, defaultOAuth2User.getAttributes()
        );

        UserPrincipal userPrincipal = createUserPrincipal(oAuth2UserInfo, provider);
        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new CustomOAuth2User(authorities, oAuth2UserInfo.getAttributes(), nameAttributeKey, userPrincipal.getUser());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOptional = userService.findOpUserByEmail(username);

        return UserPrincipal.create(userOptional.orElseThrow( ()->
                new BusinessLogicException(HttpStatus.NOT_FOUND, "email: " + username + " not found"))
        );
    }


    private UserPrincipal createUserPrincipal(OAuth2UserInfo oAuth2UserInfo, String provider) {


        Optional<User> userOptional = userService.findOpUserByEmail(oAuth2UserInfo.getEmail());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getImgUrl().equals(oAuth2UserInfo.getImgUrl()) ||
                    !user.getNickname().equals(oAuth2UserInfo.getNickname())){
                user = updateExistingUser(user, oAuth2UserInfo, provider);
            }
        } else {
            user = registerNewUser(oAuth2UserInfo, provider);
        }

        return UserPrincipal.create(user, oAuth2UserInfo.getAttributes());
    }


    private User registerNewUser(OAuth2UserInfo oauth2UserInfo, String provider) {

        List<String> authorities = authorityUtils.createRoles(oauth2UserInfo.getEmail());

        User user = User.builder()
                    .email(oauth2UserInfo.getEmail())
                    .imgUrl(oauth2UserInfo.getImgUrl())
                    .nickname(oauth2UserInfo.getNickname())
                    .provider(provider)
                    .roles(authorities)
                    .build();
        return userService.createUser(user);

    }
    private User updateExistingUser(User user, OAuth2UserInfo oauth2UserInfo, String provider) {
        List<String> authorities = authorityUtils.createRoles(user.getEmail());

        User updated = User.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(oauth2UserInfo.getNickname())
                .imgUrl(oauth2UserInfo.getImgUrl())
                .provider(provider)
                .roles(authorities)
                .build();
        return userService.updateUser(updated);
    }
}
