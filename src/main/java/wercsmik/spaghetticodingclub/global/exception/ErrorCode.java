package wercsmik.spaghetticodingclub.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Assessment
    ASSESSMENT_NOT_FOUND(404, "평가 정보가 존재하지 않습니다."),

    INVALID_ASSESSMENT_DATA(400, "평가 데이터가 유효하지 않습니다."),


    // Auth
    PASSWORD_NOT_MATCH(401, "비밀번호가 일치하지 않습니다."),

    USERNAME_ALREADY_EXIST(401, "중복된 유저네임입니다."),

    EMAIL_ALREADY_EXIST(401, "중복된 이메일입니다."),

    NO_AUTHENTICATION(401, "권한이 없습니다."),

    PASSWORD_CONFIRMATION_NOT_MATCH(401, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    USER_NOT_FOUND(400, "해당 유저가 존재하지 않습니다."),

    EMAIL_VERIFICATION_CODE_INVALID(400, "입력하신 이메일 인증 코드가 유효하지 않습니다"),

    // Scheduler


    // Team


    // Track
    USER_ALREADY_PARTICIPANT(400, "이미 트랙 참여자입니다."),

    TRACK_NOT_FOUND(400, "트랙을 찾을 수 없습니다."),

    TRACK_NAME_DUPLICATED(400, "이미 존재하는 트랙입니다."),

    INVALID_TRACK_NAME(400, "잘못된 트랙 이름입니다."),


    // Track Notice
    INVALID_NOTICE_CONTENT(400, "공지 내용이 누락되었습니다."),

    TRACK_NOTICE_NOT_FOUND(400, "해당 트랙 공지사항을 찾을 수 없습니다."),


    // Track Week
    TRACK_WEEK_OVERLAP(400, "주차 기간이 겹칩니다."),

    INVALID_DATE_RANGE(400, "종료일은 시작일보다 이전일 수 없습니다."),

    START_DATE_BEFORE_CURRENT(400, "시작일은 현재 날짜보다 이전일 수 없습니다."),


    // Unlike


    // Jwt
    INVALID_JWT_SIGNATURE(401, "유효하지 않는 JWT 서명 입니다."),

    EXPIRED_JWT_TOKEN(401, "만료된 JWT Token 입니다."),

    UNSUPPORTED_JWT_TOKEN(401, "지원되지 않는 JWT 토큰 입니다."),

    INVALID_JWT_TOKEN(401, "잘못된 JWT 토큰 입니다."),


    // User
    PASSWORD_MISMATCH_EXCEPTION(401, "비밀번호가 일치하지 않습니다."),

    PASSWORD_CONFIRMATION_EXCEPTION(401, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

    AUTHENTICATION_MISMATCH_EXCEPTION(401, "권한이 없습니다.");


    // Common

    private final int statusCode;

    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
