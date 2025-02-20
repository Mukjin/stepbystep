package io.goorm.stepbystep.controller;


import io.goorm.stepbystep.dto.HomeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeRestController {

    @GetMapping("/")
    public ResponseEntity<HomeResponse> index() {
        return ResponseEntity.ok(new HomeResponse("Welcome to the API"));
    }

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> home() {
        return ResponseEntity.ok(new HomeResponse("Home Page Data"));
    }

    @GetMapping("/admin")
    public ResponseEntity<HomeResponse> admin() {
        return ResponseEntity.ok(new HomeResponse("Admin Page Data"));
    }

}
