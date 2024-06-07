package wercsmik.spaghetticodingclub.domain.unlike.dto;

import lombok.Getter;

@Getter
public class UnlikeCreationRequestDTO {

    private Long senderUserId;

    private Long receiverUserId;

    private Long teamId;

    private String cause;
}
