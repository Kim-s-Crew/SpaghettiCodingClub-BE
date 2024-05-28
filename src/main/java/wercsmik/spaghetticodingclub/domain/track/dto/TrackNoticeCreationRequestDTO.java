package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrackNoticeCreationRequestDTO {

    private String trackNoticeTitle;

    private String trackNoticeContent;
}
