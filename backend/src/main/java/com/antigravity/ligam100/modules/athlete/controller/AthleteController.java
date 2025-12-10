package com.antigravity.ligam100.modules.athlete.controller;

import com.antigravity.ligam100.modules.athlete.domain.Athlete;
import com.antigravity.ligam100.modules.athlete.service.AthleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/athletes")
@RequiredArgsConstructor
public class AthleteController {

    private final AthleteService service;

    @GetMapping
    public ResponseEntity<List<Athlete>> getAllAthletes(@RequestParam(required = false) Long cpaId) {
        if (cpaId != null) {
            return ResponseEntity.ok(service.findByCpa(cpaId));
        }
        // TODO: En producción limitar esto para roles adecuados o paginar
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'CPA_RESPONSIBLE')")
    public ResponseEntity<Athlete> createAthlete(@RequestBody AthleteService.CreateAthleteRequest request) {
        return ResponseEntity.ok(service.create(request));
    }
}
