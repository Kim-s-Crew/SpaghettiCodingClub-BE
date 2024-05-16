package wercsmik.spaghetticodingclub.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.user.dto.ProfileResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.service.UserService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<ProfileResponseDTO>> getProfile(@PathVariable Long userId) {

        ProfileResponseDTO responseDTO = userService.getProfile(userId);

        return ResponseEntity.ok().body(CommonResponse.of("프로필 조회 성공", responseDTO));
    }
}
