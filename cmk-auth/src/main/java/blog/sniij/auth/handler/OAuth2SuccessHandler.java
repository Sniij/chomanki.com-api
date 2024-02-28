package blog.sniij.auth.handler;


import blog.sniij.auth.token.jwt.JwtProvider;
import blog.sniij.auth.util.CookieUtils;
import blog.sniij.auth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String BASE_URL = "https://blog.chomanki.com";
    private static final String LOCAL_BASE_URL = "http://localhost:3000";

    private final JwtProvider jwtProvider;

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public OAuth2SuccessHandler(JwtProvider jwtProvider, HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.jwtProvider = jwtProvider;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        if(response.isCommitted()){
            return;
        }
        String targetUrl = this.determineTargetUrl(request, response, authentication);
        this.clearAuthenticationAttributes(request);
        this.httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
        this.getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String targetUrl = BASE_URL;

        Optional<Cookie> cookie = CookieUtils.getCookie(request, "prevPage");

        if(cookie.isPresent()){
            targetUrl = targetUrl.concat("/blog/" + URLDecoder.decode(cookie.get().getValue(), StandardCharsets.UTF_8));
        }else {
            targetUrl = targetUrl.concat("/blog");
        }
/*
        Object redirect = request.getSession().getAttribute("prevPage");
        System.out.println(request.getSession().getAttribute("prevPage"));

        if(redirect != null){
            targetUrl = targetUrl.concat("/blog/" + redirect);
        }else{
            targetUrl = targetUrl.concat("/blog");
        }
*/

        /*
        Optional<Cookie> cookie = CookieUtils.getCookie(request, "prevPage");
        String targetUrl = LOCAL_BASE_URL;

        if(cookie.isPresent()){
            targetUrl = targetUrl.concat(URLDecoder.decode(cookie.get().getValue(), StandardCharsets.UTF_8));
        }else {
            targetUrl = targetUrl.concat("/blog");
        }*/


        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", jwtProvider.delegateAccessToken(authentication))
                .queryParam("refreshToken", jwtProvider.delegateRefreshToken(authentication))
                .queryParam("expiresIn", jwtProvider.getExpiration())
                .build().toUriString();
    }
}
