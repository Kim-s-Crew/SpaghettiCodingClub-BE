package wercsmik.spaghetticodingclub.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;
import wercsmik.spaghetticodingclub.global.jwt.JwtAuthenticationFilter;
import wercsmik.spaghetticodingclub.global.jwt.JwtAuthorizationFilter;
import wercsmik.spaghetticodingclub.global.jwt.JwtUtil;
import wercsmik.spaghetticodingclub.global.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final TrackParticipantsRepository trackParticipantsRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsServiceImpl, objectMapper);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil,
                trackParticipantsRepository);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));

        return filter;
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "https://spaghetticoding.shop",
                "https://www.spaghetticoding.shop",
                "https://kim.spaghetticoding.shop",
                "http://43.202.186.51:8080",
                "http://localhost:3000/",
                "http://43.202.186.51:3000",
                "http://localhost:3000",
                "https://spaghetti-coding-club.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        return configuration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // preflight 요청 허용 설정
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auths/signup").permitAll() // '/api/auths/signup' 접근 허가
                        .requestMatchers("/api/auths/login").permitAll() // '/api/auths/login' 접근 허가
                        .requestMatchers("/api/auths/withDraw").authenticated() // '/api/auths/withDraw' 요청은 인증 필요
                        .requestMatchers("/api/auths/verify-email").permitAll() // 이메일 인증 요청 허가
                        .requestMatchers("/api/auths/send-user-verification-link").permitAll() // 인증 링크 발송 요청 허가
                        .requestMatchers("/api/auths/send-recommend-verification-link").permitAll() // 인증 링크 발송 요청 허가
                        .requestMatchers("/tracks").permitAll() // '/tracks' 요청 접근 허가 (트랙 전체 조회)
                        .requestMatchers("/schedules/**").authenticated() // '/schedules/'로 시작하는 요청은 인증 필요
                        .requestMatchers("/spaghettiiii").permitAll() // aws 테스트를 위함
                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.cors(cors -> cors.configurationSource(request -> corsConfiguration()));

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
