package wercsmik.spaghetticodingclub.domain.unlike.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UnlikeCreationResponseDTO {

    private Long unlikeId;

    private Long senderUserId;

    private Long receiverUserId;

    private Long teamId;

    private String cause;

    public UnlikeCreationResponseDTO(Long unlikeId, Long senderUserId, Long receiverUserId, Long teamId, String cause) {
        this.unlikeId = unlikeId;
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.teamId = teamId;
        this.cause = cause;
    }
}
