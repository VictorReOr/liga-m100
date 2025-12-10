package com.antigravity.ligam100.modules.event.controller;

import com.antigravity.ligam100.modules.event.domain.Registration;
import com.antigravity.ligam100.modules.event.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/registrations")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService service;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'CPA_RESPONSIBLE')")
    public ResponseEntity<Registration> registerAthlete(@RequestBody CreateRegistrationRequest request) {
        return ResponseEntity.ok(service.registerAthlete(request.eventId(), request.athleteId(), request.disciplineId()));
    }

    @GetMapping
    public ResponseEntity<List<Registration>> getRegistrations(@RequestParam Long eventId) {
        return ResponseEntity.ok(service.findByEvent(eventId));
    }

    public record CreateRegistrationRequest(Long eventId, Long athleteId, Long disciplineId) {}
}
