package blog.sniij.auth.user.oauth;

import blog.sniij.domain.User;
import lombok.Builder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


public class UserPrincipal implements OAuth2User, UserDetails {

    private Collection<? extends GrantedAuthority> authorities;

    private User user;
    private transient Map<String, Object> attributes;

    @Builder
    public UserPrincipal(User user, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.attributes = attributes;
        this.authorities = authorities;
    }


    public static UserPrincipal create(User user) {

        return UserPrincipal.builder()
                .user(user)
                .build();
    }

    public static UserPrincipal create(User user, Map<String, Object> attributes) {

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        userPrincipal.setAttributes(attributes);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return userPrincipal;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.user.getUserId();
    }

    public User getUser() {
        return this.user;
    }

    public String getNickname(){
        return this.user.getNickname();
    }
}
