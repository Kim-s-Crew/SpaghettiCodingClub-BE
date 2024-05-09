package wercsmik.spaghetticodingclub.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wercsmik.spaghetticodingclub.domain.auth.dto.LoginRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

        setFilterProcessesUrl("/api/auths/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDTO requestDto = new ObjectMapper().readValue(
                    request.getInputStream(),
                    LoginRequestDTO.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException {
        log.info("로그인 성공 및 JWT 생성");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String accessToken = jwtUtil.createAccessToken(username, role);

        // CommonResponse 객체 생성
        CommonResponse commonResponse = CommonResponse.of("로그인 성공", null);

        // ObjectMapper를 사용하여 CommonResponse 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(commonResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);

        // JWT를 헤더에 추가
        jwtUtil.addJwtToHeader(accessToken, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        // 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // 응답 형식 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // CommonResponse 객체 생성
        CommonResponse commonResponse = CommonResponse.of("로그인 실패", null);

        // ObjectMapper를 사용해 CommonResponse 객체를 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(commonResponse);

        // 응답 본문에 JSON 문자열 작성
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

