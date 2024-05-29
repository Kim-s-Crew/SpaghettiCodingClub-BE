package wercsmik.spaghetticodingclub.domain.track.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrackNoticeCreationRequestDTO {

    @NotBlank(message = "트랙 공지 제목은 필수입니다.")
    private String trackNoticeTitle;

    @NotBlank(message = "트랙 공지 내용은 필수입니다.")
    private String trackNoticeContent;
}
