package com.antigravity.ligam100.modules.event.service;

import com.antigravity.ligam100.modules.athlete.domain.Athlete;
import com.antigravity.ligam100.modules.athlete.repository.AthleteRepository;
import com.antigravity.ligam100.modules.event.domain.Discipline;
import com.antigravity.ligam100.modules.event.domain.Event;
import com.antigravity.ligam100.modules.event.domain.Registration;
import com.antigravity.ligam100.modules.event.repository.DisciplineRepository;
import com.antigravity.ligam100.modules.event.repository.EventRepository;
import com.antigravity.ligam100.modules.event.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository repository;
    private final EventRepository eventRepository;
    private final AthleteRepository athleteRepository;
    private final DisciplineRepository disciplineRepository;

    @Transactional
    public Registration registerAthlete(Long eventId, Long athleteId, Long disciplineId) {
        if (repository.existsByEventoIdAndDeportistaIdAndDisciplinaId(eventId, athleteId, disciplineId)) {
            throw new RuntimeException("El deportista ya está inscrito en esta disciplina");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        
        Athlete athlete = athleteRepository.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("Deportista no encontrado"));
        
        Discipline discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));

        // 1. Validar máximo de pruebas
        List<Registration> existingRegistrations = repository.findByEventoIdAndDeportistaId(eventId, athleteId);
        if (existingRegistrations.size() >= event.getMaxPruebasPorDeportista()) {
            throw new RuntimeException("El deportista ha alcanzado el límite de " + event.getMaxPruebasPorDeportista() + " pruebas.");
        }

        // 2. Validar solapamiento de horarios
        for (Registration reg : existingRegistrations) {
            Discipline existingDiscipline = reg.getDisciplina();
            if (isOverlapping(discipline, existingDiscipline)) {
                throw new RuntimeException("Conflicto de horario con la prueba: " + existingDiscipline.getNombre());
            }
        }

        // 3. Crear inscripción
        Registration registration = Registration.builder()
                .evento(event)
                .deportista(athlete)
                .disciplina(discipline)
                .build();
        
        return repository.save(registration);
    }

    private boolean isOverlapping(Discipline d1, Discipline d2) {
        if (d1.getHorarioInicio() == null || d1.getHorarioFin() == null ||
            d2.getHorarioInicio() == null || d2.getHorarioFin() == null) {
            return false;
        }
        return d1.getHorarioInicio().isBefore(d2.getHorarioFin()) &&
               d2.getHorarioInicio().isBefore(d1.getHorarioFin());
    }

    public List<Registration> findByEvent(Long eventId) {
        return repository.findByEventoId(eventId);
    }
}
