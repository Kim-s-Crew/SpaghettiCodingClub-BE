package wercsmik.spaghetticodingclub.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Assesment

    // Auth
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다.");

    // Scheduler

    // Team

    // Track

    // Unlike

    // User

    // Common

    private final int statusCode;

    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
