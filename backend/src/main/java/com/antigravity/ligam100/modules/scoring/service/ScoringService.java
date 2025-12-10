package com.antigravity.ligam100.modules.scoring.service;

import com.antigravity.ligam100.modules.discipline.domain.Discipline;
import com.antigravity.ligam100.modules.discipline.rules.ScoringStrategy;
import com.antigravity.ligam100.modules.scoring.domain.Score;
import com.antigravity.ligam100.modules.scoring.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private final ScoreRepository scoreRepository;
    private final List<ScoringStrategy> strategies;

    public void calculateAndSaveScore(Score score, Discipline discipline, int position) {
        Map<String, ScoringStrategy> strategyMap = strategies.stream()
                .collect(Collectors.toMap(ScoringStrategy::getStrategyType, Function.identity()));

        Map<String, Object> config = discipline.getScoringConfig();
        if (config == null) return;

        String type = (String) config.get("type");
        ScoringStrategy strategy = strategyMap.get(type);

        if (strategy != null) {
            Map<String, Object> context = Map.of("position", position);
            BigDecimal points = strategy.calculatePoints(score.getRawScore(), config, context);
            score.setValidatedScore(points);
            scoreRepository.save(score);
        }
    }
}
