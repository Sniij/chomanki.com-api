package blog.sniij.api;

import blog.sniij.auth.token.jwt.JwtProvider;
import blog.sniij.dto.SingleResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final JwtProvider jwtProvider;

    public AuthController(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }


    @GetMapping("/refresh")
    public ResponseEntity<SingleResponseDto<Map<String, String>>> refreshAccessToken(
            @RequestHeader("Refresh") String refreshToken
    ) {

        Map<String, String> map = new HashMap<>();
        map.put("accessToken", jwtProvider.getAccessTokenByRefreshToken(refreshToken));

        return new ResponseEntity<>(new SingleResponseDto<>(map),HttpStatus.CREATED);
    }
}
