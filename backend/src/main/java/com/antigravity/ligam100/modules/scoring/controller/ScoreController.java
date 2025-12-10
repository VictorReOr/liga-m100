package com.antigravity.ligam100.modules.scoring.controller;

import com.antigravity.ligam100.modules.scoring.domain.Score;
import com.antigravity.ligam100.modules.scoring.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService service;

    @PostMapping("/record")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'JUDGE')")
    public ResponseEntity<Score> recordScore(@RequestBody RecordScoreRequest request) {
        return ResponseEntity.ok(service.recordScore(
                request.registrationId(),
                request.judgeId(),
                request.marca(),
                request.marcaTexto(),
                request.notas()
        ));
    }

    @GetMapping("/discipline/{disciplineId}")
    public ResponseEntity<List<Score>> getScoresByDiscipline(@PathVariable Long disciplineId) {
        return ResponseEntity.ok(service.getByDiscipline(disciplineId));
    }

    public record RecordScoreRequest(Long registrationId, Long judgeId, BigDecimal marca, String marcaTexto, String notas) {}
}
