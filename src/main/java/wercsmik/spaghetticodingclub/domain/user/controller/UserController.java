package wercsmik.spaghetticodingclub.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.user.dto.ProfileResponseDTO;
import wercsmik.spaghetticodingclub.domain.user.dto.UpdateUserNameRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.dto.UpdateUserPasswordRequestDTO;
import wercsmik.spaghetticodingclub.domain.user.entity.User;
import wercsmik.spaghetticodingclub.domain.user.service.UserService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;
import wercsmik.spaghetticodingclub.global.security.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<CommonResponse<ProfileResponseDTO>> getMyProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ProfileResponseDTO responseDTO = userService.getMyProfile(userDetails);

        return ResponseEntity.ok().body(CommonResponse.of("본인 프로필 조회 성공", responseDTO));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<ProfileResponseDTO>> getUserProfile(@PathVariable Long userId) {

        ProfileResponseDTO responseDTO = userService.getUserProfile(userId);

        return ResponseEntity.ok().body(CommonResponse.of("특정 유저 프로필 조회 성공", responseDTO));
    }

    @PatchMapping("/{userId}/password")
    public ResponseEntity<CommonResponse<Void>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserPasswordRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        userService.updatePassword(userId, requestDTO, loginUser);

        return ResponseEntity.ok().body(CommonResponse.of("비밀번호 수정 성공", null));
    }

    @PatchMapping("/{userId}/username")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CommonResponse<Void>> updateName(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateUserNameRequestDTO requestDTO,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User loginUser = userDetails.getUser();
        userService.updateUserName(userId, requestDTO, loginUser);

        return ResponseEntity.ok().body(CommonResponse.of("사용자 이름 변경 성공", null));
    }
}
