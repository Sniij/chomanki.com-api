package blog.sniij.auth.token.filter;

import blog.sniij.auth.token.jwt.JwtTokenizer;
import blog.sniij.auth.token.util.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (SignatureException se){
            request.setAttribute("exception", se); // JWT 서명 검증 실패
        } catch (ExpiredJwtException ee){
            request.setAttribute("exception", ee); // JWT 만료시 발생
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }


        filterChain.doFilter(request, response);
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws =request.getHeader("Authorization").replace("Bearer ","");

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getPayload();

        return claims;
    }


    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("email");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException{
        String authorization = request.getHeader("Authorization");

        return authorization == null;
    }


}