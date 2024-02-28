package blog.sniij.auth.handler;


import blog.sniij.auth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final String ERROR_PARAM = "?error=";
    private static final String BASE_URL = "https://blog.chomanki.com";
    private static final String LOCAL_BASE_URL = "http://localhost:3000";

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    public OAuth2FailureHandler(HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        String targetUrl = getAuthorizedTargetUrl(exception);
        httpCookieOAuth2AuthorizationRequestRepository.clearCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);

    }


    private String getAuthorizedTargetUrl(AuthenticationException exception) {

        StringBuilder targetUrl = new StringBuilder();
        targetUrl.append(BASE_URL);

        String encodedErrorMessage = URLEncoder.encode(getExceptionMessage(exception), StandardCharsets.UTF_8);
        targetUrl.append(ERROR_PARAM).append(encodedErrorMessage);

        return targetUrl.toString();

    }


    private String getExceptionMessage(AuthenticationException exception) {

        if (exception instanceof OAuth2AuthenticationException) {
            return ((OAuth2AuthenticationException) exception).getError().getErrorCode();
        } else {
            return exception.getLocalizedMessage();
        }
    }

}
