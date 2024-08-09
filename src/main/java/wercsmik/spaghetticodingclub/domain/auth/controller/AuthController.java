package wercsmik.spaghetticodingclub.domain.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.auth.dto.DeleteRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.EmailCheckRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.SignRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.service.AuthService;
import wercsmik.spaghetticodingclub.domain.auth.service.EmailService;
import wercsmik.spaghetticodingclub.domain.user.repository.UserRepository;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/auths")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, EmailService emailService,
            UserRepository userRepository) {
        this.authService = authService;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Void>> signup(
            @Valid @RequestBody SignRequestDTO signRequestDTO) {

        authService.signup(signRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("회원가입이 완료되었습니다.", null));
    }

    @PostMapping("/send-user-verification-link")
    public ResponseEntity<CommonResponse<Void>> sendUserVerificationLink(
            @Valid @RequestBody EmailCheckRequestDTO request) {

        emailService.sendVerificationLink(request.getEmail(), "USER_EMAIL", null);
        String message = String.format("%s 메일로 인증 링크 발송 성공", request.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of(message, null));
    }

    @PostMapping("/send-recommend-verification-link")
    public ResponseEntity<CommonResponse<Void>> sendRecommendVerificationLink(
            @Valid @RequestBody EmailCheckRequestDTO request) {
        if (!emailService.isEmailVerified(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.of("본인 메일 인증 후 다시 시도 바랍니다.", null));
        }

        emailService.sendVerificationLink(request.getRecommendEmail(), "RECOMMEND_EMAIL", request.getEmail());
        String message = String.format("%s 메일로 인증 링크 발송 성공", request.getRecommendEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of(message, null));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<CommonResponse<Void>> verifyEmail(
            @RequestParam("token") String token) {

        System.out.println("Email verification request received for token: " + token);
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(CommonResponse.of("이메일 인증 성공. 다시 회원가입 버튼을 눌러주세요.", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(CommonResponse.of("인증 토큰이 유효하지 않습니다", null));
        }
    }

    @DeleteMapping("/withDraw")
    public ResponseEntity<CommonResponse<Void>> withDrawUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {

        authService.withDrawUser(userDetails, deleteRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("유저 삭제 성공", null));
    }
}