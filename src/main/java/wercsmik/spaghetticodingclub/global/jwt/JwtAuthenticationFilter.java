package wercsmik.spaghetticodingclub.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
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

    private static final String CONTENT_TYPE = "application/json";

    private static final String CHARACTER_ENCODING = "UTF-8";

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;

        setFilterProcessesUrl("/api/auths/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDTO requestDto = null;
        try {
            requestDto = new ObjectMapper().readValue(
                    request.getInputStream(),
                    LoginRequestDTO.class);
            log.info("로그인 시도: {}", requestDto.getEmail()); // 로그인 시도 로깅 추가

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error("로그인 시도 중 에러 발생: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (AuthenticationException e) {
            log.info("옳지않은 비밀번호: {}", Objects.requireNonNull(requestDto).getPassword()); // 비밀번호가 틀렸을 때 로깅 추가
            throw e;
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

        jwtUtil.addJwtToHeader(accessToken, response);

        // CommonResponse 객체 생성
        CommonResponse commonResponse = CommonResponse.of("로그인 성공", null);

        writeResponse(response, commonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        CommonResponse commonResponse = CommonResponse.of("로그인 실패", null);

        writeResponse(response, commonResponse);
    }

    private void writeResponse(HttpServletResponse response, CommonResponse commonResponse) throws IOException {
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(CHARACTER_ENCODING);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(commonResponse);

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}

