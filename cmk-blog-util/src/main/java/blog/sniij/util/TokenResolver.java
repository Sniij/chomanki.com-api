package blog.sniij.util;

import blog.sniij.exception.BusinessLogicException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.util.Base64;

public interface TokenResolver {

    default String getPayloadString(String token){
        String[] parts = token.split("\\.");
        String payload = parts[1];
        byte[] payloadBytes = Base64.getUrlDecoder().decode(payload);

        return new String(payloadBytes);
    }

    String getSubject(String subject);

    default String resolveAccessTokenOrNull(HttpServletRequest request) {

        String token = null;
        token = request.getHeader("Authorization");

        if (token != null)
            return token;
        else
            throw new BusinessLogicException(HttpStatus.BAD_REQUEST,  "Invalid access token");
    }



}
