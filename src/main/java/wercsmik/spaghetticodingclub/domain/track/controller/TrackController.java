package wercsmik.spaghetticodingclub.domain.track.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wercsmik.spaghetticodingclub.domain.track.dto.TrackResponseDTO;
import wercsmik.spaghetticodingclub.domain.track.service.TrackService;
import wercsmik.spaghetticodingclub.global.auditing.BaseTimeEntity;
import wercsmik.spaghetticodingclub.global.common.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tracks")
public class TrackController extends BaseTimeEntity {

    private final TrackService trackService;

    @GetMapping
    public ResponseEntity<CommonResponse<List<TrackResponseDTO>>> getAllTracks() {

        List<TrackResponseDTO> tracks = trackService.getAllTracks();

        return ResponseEntity.ok().body(CommonResponse.of("트랙 전체 조회 성공", tracks));
    }
}
