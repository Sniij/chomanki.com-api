package blog.sniij.auth.config;



import blog.sniij.auth.handler.OAuth2AuthenticationEntryPoint;
import blog.sniij.auth.handler.OAuth2FailureHandler;
import blog.sniij.auth.handler.OAuth2SuccessHandler;
import blog.sniij.auth.token.filter.JwtVerificationFilter;
import blog.sniij.auth.token.jwt.JwtTokenizer;
import blog.sniij.auth.token.util.CustomAuthorityUtils;
import blog.sniij.auth.user.oauth.CustomOAuth2UserService;
import blog.sniij.auth.util.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService userService;

    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private final OAuth2FailureHandler oAuth2FailureHandler;

    private final HttpCookieOAuth2AuthorizationRequestRepository repository;

    private final OAuth2AuthorizedClientService authorizedClientService;


    private final JwtTokenizer jwtTokenizer;

    private final CustomAuthorityUtils authorityUtils;

    public SecurityConfig(CustomOAuth2UserService userService, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2FailureHandler oAuth2FailureHandler, HttpCookieOAuth2AuthorizationRequestRepository repository, OAuth2AuthorizedClientService authorizedClientService, JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {
        this.userService = userService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2FailureHandler = oAuth2FailureHandler;
        this.repository = repository;
        this.authorizedClientService = authorizedClientService;
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(config->config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtVerificationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/comment/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/comment/**").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/comment/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/user").authenticated()

                        .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                )
                .exceptionHandling(exceptionHandlingConfig->
                        exceptionHandlingConfig.authenticationEntryPoint(new OAuth2AuthenticationEntryPoint()))
                .oauth2Login(oAuth2LoginConfig ->
                        oAuth2LoginConfig
                                .successHandler(oAuth2SuccessHandler)
                                .failureHandler(oAuth2FailureHandler)
                                .authorizedClientService(authorizedClientService)
                                .authorizationEndpoint(config->config
                                        .baseUri("/login/oauth2/authorization")
                                        .authorizationRequestRepository(repository)
                                )
                                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(userService))

                );

        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("https://blog.chomanki.com");

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public JwtVerificationFilter jwtVerificationFilter(){
        return new JwtVerificationFilter(jwtTokenizer, authorityUtils);
    }


/*    @Component
    public static class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException {
            String uri = (String) request.getSession().getAttribute("requesturl");
            getRedirectStrategy().sendRedirect(request, response, uri);
        }
    }*/
}