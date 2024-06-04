package wercsmik.spaghetticodingclub.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.auth.dto.EmailCheckRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.SignRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.service.AuthService;
import wercsmik.spaghetticodingclub.domain.auth.service.EmailService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

@RestController
@RequestMapping("/api/auths")
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;

    public AuthController(AuthService authService, EmailService emailService) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Void>> signup(
            @Valid @RequestBody SignRequestDTO signRequestDTO) {

        authService.signup(signRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("회원가입 성공", null));
    }

    @PatchMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(HttpServletRequest request) {

        authService.logout(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("로그아웃 성공", null));
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<CommonResponse<Void>> mailConfirm(
            @RequestParam Long userId, @RequestBody EmailCheckRequestDTO emailCheckRequestDTO) {

        emailService.sendVerificationEmail(userId, emailCheckRequestDTO.getEmail());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("인증 이메일 전송 성공", null));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<CommonResponse<Void>> verifyEmail(
            @RequestBody EmailCheckRequestDTO requestDTO) {

        emailService.verifyEmail(requestDTO.getVerificationCode(), requestDTO.getEmail());

        return ResponseEntity.ok().body(CommonResponse.of("이메일 인증 성공", null));
    }
}