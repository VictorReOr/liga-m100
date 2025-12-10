package com.antigravity.ligam100.modules.event.service;

import com.antigravity.ligam100.modules.event.domain.Discipline;
import com.antigravity.ligam100.modules.event.domain.Event;
import com.antigravity.ligam100.modules.event.repository.DisciplineRepository;
import com.antigravity.ligam100.modules.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineService {

    private final DisciplineRepository repository;
    private final EventRepository eventRepository;

    public List<Discipline> findByEvent(Long eventId) {
        return repository.findByEventoId(eventId);
    }

    public Discipline create(Discipline discipline, Long eventId) {
        if (eventId != null) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
            discipline.setEvento(event);
        }
        return repository.save(discipline);
    }
    
    public Discipline findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Disciplina no encontrada"));
    }
}
