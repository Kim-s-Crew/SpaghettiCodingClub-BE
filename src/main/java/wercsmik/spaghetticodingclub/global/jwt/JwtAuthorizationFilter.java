package wercsmik.spaghetticodingclub.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsServiceImpl;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtUtil.resolveAccessToken(request);

        try {
            if (accessToken != null && jwtUtil.validateToken(accessToken)) {
                Claims info = jwtUtil.getUserInfoFromToken(accessToken);

                // 인증정보에 유저정보 넣기
                String username = info.getSubject();
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);
            } else if (accessToken != null) { // accessToken이 null이 아니지만 유효하지 않은 토큰일 경우
                sendErrorResponse(response, "토큰이 유효하지 않습니다.", HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, "토큰이 만료되었습니다.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        } catch (MalformedJwtException e) {
            sendErrorResponse(response, "토큰 형식이 잘못되었습니다.", HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (SignatureException e) {
            sendErrorResponse(response, "토큰 서명이 잘못되었습니다.", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // accessToken이 null이거나 인증 처리가 완료됐다면, 다음 필터로 요청과 응답을 넘김
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        CommonResponse commonResponse = new CommonResponse(message, status);
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }
}