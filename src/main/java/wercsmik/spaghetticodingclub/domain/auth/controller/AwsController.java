package wercsmik.spaghetticodingclub.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsController {

    @GetMapping("/spaghettiiii")
    public ResponseEntity<?> spaghetti() {

        return ResponseEntity.ok().body("Hello, Spaghetti!");
    }
}
