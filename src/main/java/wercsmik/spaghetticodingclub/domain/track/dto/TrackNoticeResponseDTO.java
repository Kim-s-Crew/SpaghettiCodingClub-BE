package wercsmik.spaghetticodingclub.domain.track.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import wercsmik.spaghetticodingclub.domain.track.entity.TrackNotice;

@Getter
@AllArgsConstructor
public class TrackNoticeResponseDTO {

    private Long noticeId;

    private Long trackId;

    private Long userId;

    private String trackNoticeTitle;

    private String trackNoticeContent;

    public TrackNoticeResponseDTO(TrackNotice trackNotice) {
        this.noticeId = trackNotice.getNoticeId();
        this.trackId = trackNotice.getTrack().getTrackId();
        this.userId = trackNotice.getUser().getUserId();
        this.trackNoticeTitle = trackNotice.getTrackNoticeTitle();
        this.trackNoticeContent = trackNotice.getTrackNoticeContent();
    }
}
