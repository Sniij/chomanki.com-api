package blog.sniij.auth.token.jwt;

import blog.sniij.auth.user.oauth.CustomOAuth2User;
import blog.sniij.auth.user.oauth.UserPrincipal;
import blog.sniij.domain.User;
import blog.sniij.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtProvider {

    private final JwtTokenizer jwtTokenizer;

    private final UserService userService;

    public JwtProvider(JwtTokenizer jwtTokenizer, UserService userService) {
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
    }

    public Object delegateAccessToken(Authentication authentication){

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        Map<String , Object> claims = new HashMap<>();
        claims.put("email",user.getEmail());
        claims.put("roles", user.getRoles());

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String subject = user.getUserId();


        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    public Object delegateRefreshToken(Authentication authentication) {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String subject = user.getUserId();

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }

    public int getExpiration() {
        return jwtTokenizer.getAccessTokenExpirationMinutes();
    }


    public Authentication getAuthentication(String requestToken){
        String jws = requestToken.replace("Bearer ","");
        Jws<Claims> claims = jwtTokenizer.getClaims(jws, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()));
        String role = (String) claims.getPayload().get("roles");
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(role));


        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getPayload().getSubject(), "", authorities), jws, authorities);
    }

    public User getUserByRequestToken(String requestToken) {
        String jws = requestToken.replace("Bearer ","");
        Jws<Claims> claims = jwtTokenizer.getClaims(jws, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()));

        return userService.findUserById(claims.getPayload().getSubject());
    }
    public User getUserByRefreshToken(String refreshToken) {

        Jws<Claims> claims = jwtTokenizer.getClaims(refreshToken, jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()));

        return userService.findUserById(claims.getPayload().getSubject());
    }

    public String getAccessTokenByRefreshToken(String refreshToken) {
        User user = getUserByRefreshToken(refreshToken);
        return jwtTokenizer.refreshAccessToken(refreshToken, user);
    }
}