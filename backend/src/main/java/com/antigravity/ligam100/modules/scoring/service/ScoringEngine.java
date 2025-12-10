package com.antigravity.ligam100.modules.scoring.service;

import com.antigravity.ligam100.modules.event.domain.Discipline;
import com.antigravity.ligam100.modules.scoring.domain.Score;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ScoringEngine {

    /*
     * Esta clase contiene la lógica para calcular puntos basándose en las reglas JSON de la disciplina.
     * Reglas soportadas (simplificadas para MVP):
     * - POSITION: Asigna puntos fijos [10, 8, 6...] según la posición ordenada.
     * 
     * En una versión completa esto sería mucho más complejo y soportaría más tipos.
     */

    public void calculatePointsForDiscipline(Discipline discipline, List<Score> scores) {
        Map<String, Object> rules = discipline.getReglasConfig();
        if (rules == null || !rules.containsKey("type")) {
            return; // No hay reglas configuradas
        }

        String type = (String) rules.get("type");

        if ("POSITION".equalsIgnoreCase(type)) {
            calculatePositionBasedPoints(discipline, scores, rules);
        }
        // Aquí se agregarían más estrategias (TIME, DISTANCE, etc. directa)
    }

    private void calculatePositionBasedPoints(Discipline discipline, List<Score> scores, Map<String, Object> rules) {
        @SuppressWarnings("unchecked")
        List<Integer> pointsTable = (List<Integer>) rules.get("points"); // Ej: [10, 8, 6, 5, 4, 3, 2, 1]

        if (pointsTable == null || pointsTable.isEmpty()) return;

        // Ordenar scores
        // Dependiendo del tipo de marca, mayor es mejor (DISTANCIA) o menor es mejor (TIEMPO)
        // Por defecto asumimos TIEMPO (menor es mejor) a menos que se especifique
        boolean higherIsBetter = "DISTANCIA".equalsIgnoreCase(discipline.getTipoMarca()) || 
                                 "PUNTUACION".equalsIgnoreCase(discipline.getTipoMarca());

        scores.sort((s1, s2) -> {
            if (s1.getMarca() == null && s2.getMarca() == null) return 0;
            if (s1.getMarca() == null) return 1;
            if (s2.getMarca() == null) return -1;
            
            return higherIsBetter ? 
                   s2.getMarca().compareTo(s1.getMarca()) : // Descendente
                   s1.getMarca().compareTo(s2.getMarca());  // Ascendente
        });

        // Asignar puntos
        for (int i = 0; i < scores.size() && i < pointsTable.size(); i++) {
            Score score = scores.get(i);
            if (score.isEsValida() && score.getMarca() != null) {
                score.setCalculoPuntos(pointsTable.get(i));
            } else {
                score.setCalculoPuntos(0);
            }
        }
    }
}
