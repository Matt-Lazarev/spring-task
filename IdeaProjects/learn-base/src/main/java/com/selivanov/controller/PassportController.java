package com.selivanov.controller;

import com.selivanov.dto.PassportDto;
import com.selivanov.service.PassportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/passports")
@RequiredArgsConstructor
public class PassportController {
    private final PassportService service;

    @PostMapping
    public ResponseEntity<?> savePassport(@Valid @RequestBody PassportDto passportDto) {
        service.savePassport(passportDto);
        return ResponseEntity.ok().build();
    }
}