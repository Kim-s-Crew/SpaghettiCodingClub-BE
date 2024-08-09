package wercsmik.spaghetticodingclub.domain.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class EmailCheckRequestDTO {

    @Email(message = "옳지 않은 메일 주소입니다. 다시 입력해주세요")
    private String email;

    @Email(message = "옳지 않은 메일 주소입니다. 다시 입력해주세요")
    private String recommendEmail;
}
