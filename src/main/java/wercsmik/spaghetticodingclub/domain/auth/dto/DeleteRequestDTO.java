package wercsmik.spaghetticodingclub.domain.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DeleteRequestDTO {

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
