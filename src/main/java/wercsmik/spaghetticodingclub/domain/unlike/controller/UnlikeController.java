package wercsmik.spaghetticodingclub.domain.unlike.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.unlike.dto.UnlikeCreationRequestDTO;
import wercsmik.spaghetticodingclub.domain.unlike.dto.UnlikeCreationResponseDTO;
import wercsmik.spaghetticodingclub.domain.unlike.service.UnlikeService;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

@AllArgsConstructor
@RestController
@RequestMapping("/unlike")
public class UnlikeController {

    private final UnlikeService unlikeService;

    @PostMapping
    public ResponseEntity<CommonResponse<UnlikeCreationResponseDTO>> createUnlike(
            @RequestBody UnlikeCreationRequestDTO requestDTO) {

        UnlikeCreationResponseDTO unlike = unlikeService.createUnlike(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.of("비호감도 생성 성공", unlike));
    }

}
