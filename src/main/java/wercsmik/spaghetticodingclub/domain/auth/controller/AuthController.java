package wercsmik.spaghetticodingclub.domain.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.auth.dto.DeleteRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.dto.SignRequestDTO;
import wercsmik.spaghetticodingclub.domain.auth.service.AuthService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/auths")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Void>> signup(
            @Valid @RequestBody SignRequestDTO signRequestDTO) {

        authService.signup(signRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("회원가입 성공", null));
    }

    @DeleteMapping("/withDraw")
    public ResponseEntity<CommonResponse<Void>> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody DeleteRequestDTO deleteRequestDTO) {

        authService.deleteUser(userDetails, deleteRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.of("유저 삭제 성공", null));
    }
}