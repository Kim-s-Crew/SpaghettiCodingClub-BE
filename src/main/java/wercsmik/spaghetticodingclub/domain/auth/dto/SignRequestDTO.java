package wercsmik.spaghetticodingclub.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequestDTO {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{1,10}$",
            message = "올바른 형식의 이름이 아닙니다. 다시 한번 확인 후 작성해주세요.")
    private String username;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{8,}$",
            message = "올바른 형식의 비밀번호가 아닙니다. 문자(대문자/소문자) 혹은 숫자를 8글자 이상 작성해주세요.")
    private String password;

    private String checkPassword;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,})+$",
            message = "올바른 이메일 형식이 아닙니다. 문자(대문자/소문자)@도메인으로 입력해주세요.")
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{2,})+$",
            message = "올바른 이메일 형식이 아닙니다. 문자(대문자/소문자)@도메인으로 입력해주세요.")
    private String recommendEmail;

    private String track;

}
