package wercsmik.spaghetticodingclub.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wercsmik.spaghetticodingclub.domain.auth.dto.LoginRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.LoginResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackParticipants;
import wercsmik.spaghetticodingclub.domain.track.repository.TrackParticipantsRepository;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.entity.UserRoleEnum;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private final JwtUtil jwtUtil;
    private final TrackParticipantsRepository trackParticipantsRepository;
    public JwtAuthenticationFilter(JwtUtil jwtUtil,
            TrackParticipantsRepository trackParticipantsRepository) {
        this.jwtUtil = jwtUtil;
        this.trackParticipantsRepository = trackParticipantsRepository;

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

        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String email = user.getEmail();
        UserRoleEnum role = user.getRole();

        // 인증 여부 확인
        if (!user.isVerified()) {
            // 사용자가 인증되지 않은 경우
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            CommonResponse commonResponse = CommonResponse.of("인증이 필요합니다", null);
            writeResponse(response, commonResponse);
            return;
        }

        String accessToken = jwtUtil.createAccessToken(email, role);

        jwtUtil.addJwtToHeader(accessToken, response);

        // 사용자가 참여하고 있는 트랙 조회
        List<TrackParticipants> trackParticipants = trackParticipantsRepository.findByUserUserId(user.getUserId());
        String trackName = trackParticipants.stream().findFirst()
                .map(participant -> participant.getTrack().getTrackName())
                .orElse("참여된 트랙 없음");
        Long trackId = trackParticipants.stream().findFirst()
                .map(participant -> participant.getTrack().getTrackId())
                .orElse(null); // 트랙 ID 가져오기

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getUserId(), user.getUsername(), user.getEmail(), trackName, trackId, user.getRole(), user.getRecommendEmail());

        // CommonResponse 객체 생성
        CommonResponse<LoginResponseDTO> commonResponse = CommonResponse.of("로그인 성공", loginResponseDTO);

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

