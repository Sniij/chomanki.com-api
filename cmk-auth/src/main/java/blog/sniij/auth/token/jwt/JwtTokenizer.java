package blog.sniij.auth.token.jwt;

import blog.sniij.auth.user.oauth.UserPrincipal;
import blog.sniij.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;



    public String encodeBase64SecretKey(String secretKey){
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Object getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Map<String , Object> claims,
                                      String subject,
                                      Date expiration,
                                      String baseEncodedSecretKey){

        Key key = (Key) getKeyFromBase64EncodedKey(baseEncodedSecretKey);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(Calendar.getInstance().getTime())
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String subject,
                                       Date expiration,
                                       String baseEncodedSecretKey){

        Key key = (Key) getKeyFromBase64EncodedKey(baseEncodedSecretKey);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Calendar.getInstance().getTime())
                .expiration(expiration)
                .signWith(key)
                .compact();
    }


    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey){
        SecretKey key = (SecretKey) getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jws);

            return claims;
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    public void verifySignature(String jws, String base64EncodedSecretKey){
        SecretKey key = (SecretKey) getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jws);
    }


    public Date getTokenExpiration(int expirationMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    public String refreshAccessToken(String refreshToken, User user){

        if(verifyTokenExpiration(refreshToken)){
            Map<String , Object> claims = new HashMap<>();
            claims.put("email",user.getEmail());
            claims.put("roles", user.getRoles());

            String subject = user.getUserId();
            Date expiration = getTokenExpiration(getAccessTokenExpirationMinutes());

            String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

            return generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
        }

        return null;
    }

    public boolean verifyTokenExpiration(String requestToken) {
        if (requestToken != null) {
            Jws<Claims> claims = getClaims(requestToken, encodeBase64SecretKey(secretKey));
            Date expiration = null;
            if(claims!=null)
                expiration = claims.getPayload().getExpiration();

            if (expiration != null) {
                Date now = new Date();
                return now.before(expiration);
                // 현재 시간이 토큰의 만료 시간 이전인지 확인
            }
        }
        return false;
    }

}