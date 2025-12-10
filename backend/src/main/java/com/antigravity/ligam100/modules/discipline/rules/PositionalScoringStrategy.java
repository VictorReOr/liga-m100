package com.antigravity.ligam100.modules.discipline.rules;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

/**
 * Assigns points based on the position in the ranking.
 * <p>
 * Config example:
 * {
 *   "type": "POSITIONAL",
 *   "points": [100, 90, 80, 70, 60, 50, ...]
 * }
 * </p>
 */
@Component
public class PositionalScoringStrategy implements ScoringStrategy {

    @Override
    public String getStrategyType() {
        return "POSITIONAL";
    }

    @Override
    public BigDecimal calculatePoints(BigDecimal rawScore, Map<String, Object> config, Map<String, Object> context) {
        Integer position = (Integer) context.get("position");
        if (position == null || position < 1) {
            return BigDecimal.ZERO;
        }

        @SuppressWarnings("unchecked")
        List<Number> pointsTable = (List<Number>) config.get("points");

        if (pointsTable != null && position <= pointsTable.size()) {
            return new BigDecimal(pointsTable.get(position - 1).toString());
        }
        
        return BigDecimal.ZERO; // Or some default minimum
    }
}
