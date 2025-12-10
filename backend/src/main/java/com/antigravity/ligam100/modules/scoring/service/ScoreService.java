package com.antigravity.ligam100.modules.scoring.service;

import com.antigravity.ligam100.modules.auth.domain.User;
import com.antigravity.ligam100.modules.auth.repository.UserRepository;
import com.antigravity.ligam100.modules.event.domain.Registration;
import com.antigravity.ligam100.modules.event.repository.RegistrationRepository;
import com.antigravity.ligam100.modules.scoring.domain.Score;
import com.antigravity.ligam100.modules.scoring.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreRepository repository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ScoringEngine scoringEngine;

    @Transactional
    public Score recordScore(Long registrationId, Long judgeId, BigDecimal marca, String marcaTexto, String notas) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        User judge = null;
        if (judgeId != null) {
            judge = userRepository.findById(judgeId)
                    .orElseThrow(() -> new RuntimeException("Juez no encontrado"));
            // Validar si el usuario es Juez
            // if (judge.getRole() != UserRole.JUDGE) ...
        }

        // Buscar si ya existe puntuación para actualizar
        Score score = repository.findByInscripcionId(registrationId)
                .orElse(Score.builder()
                        .inscripcion(registration)
                        .juez(judge)
                        .build());

        score.setMarca(marca);
        score.setMarcaTexto(marcaTexto);
        score.setNotas(notas);
        score.setJuez(judge);
        score.setEsValida(true);

        // Guardar primero
        score = repository.save(score);

        // Recalcular puntos de la disciplina completa
        // ESTO ES INTENSIVO: en prod se haría async o al cerrar la prueba
        updateDisciplineRankings(registration.getDisciplina().getId());
        
        return score;
    }

    private void updateDisciplineRankings(Long disciplineId) {
        List<Score> scores = repository.findByDisciplinaId(disciplineId);
        if (scores.isEmpty()) return;
        
        scoringEngine.calculatePointsForDiscipline(scores.get(0).getInscripcion().getDisciplina(), scores);
        repository.saveAll(scores);
    }
    
    public List<Score> getByDiscipline(Long disciplineId) {
        return repository.findByDisciplinaId(disciplineId);
    }
}
