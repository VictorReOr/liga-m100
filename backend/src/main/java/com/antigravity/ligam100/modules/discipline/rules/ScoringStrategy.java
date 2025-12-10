package com.antigravity.ligam100.modules.discipline.rules;

import com.antigravity.ligam100.modules.discipline.domain.Discipline;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Strategy interface for calculating points based on raw scores/results.
 */
public interface ScoringStrategy {
    
    /**
     * Calculate the points considering the configuration and the raw score.
     * 
     * @param rawScore The raw value (time, distance, etc)
     * @param config The scoring configuration from the Discipline
     * @param context Additional context if needed (e.g. position in ranking)
     * @return The calculated points
     */
    BigDecimal calculatePoints(BigDecimal rawScore, Map<String, Object> config, Map<String, Object> context);

    String getStrategyType();
}
