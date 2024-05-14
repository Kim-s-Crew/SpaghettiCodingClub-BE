package wercsmik.spaghetticodingclub.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Assesment

    // Auth
    PASSWORD_NOT_MATCH(401, "비밀번호가 일치하지 않습니다."),

    USERNAME_ALREADY_EXIST(401, "중복된 유저네임입니다."),

    KAKAOID_ALREADY_EXIST(401, "중복된 카카오ID 입니다."),

    EMAIL_ALREADY_EXIST(401, "중복된 이메일입니다."),

    NO_AUTHENTICATION(401, "권한이 없습니다."),

    PASSWORD_CONFIRMATION_NOT_MATCH(401, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    USER_NOT_FOUND(400, "해당 유저가 존재하지 않습니다"),

    EMAIL_VERIFICATION_CODE_INVALID(400, "입력하신 이메일 인증 코드가 유효하지 않습니다"),

    KAKAO_USER_EMAIL_MODIFICATION_EXCEPTION(400, "카카오 로그인한 유저는 이메일을 수정할 수 없습니다."),

    // Scheduler

    // Team

    // Track

    // Unlike

    //Jwt
    INVALID_JWT_SIGNATURE(401, "유효하지 않는 JWT 서명 입니다."),

    EXPIRED_JWT_TOKEN(401, "만료된 JWT Token 입니다."),

    UNSUPPORTED_JWT_TOKEN(401, "지원되지 않는 JWT 토큰 입니다."),

    INVALID_JWT_TOKEN(401, "잘못된 JWT 토큰 입니다.");

    // User


    // Common

    private final int statusCode;

    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
