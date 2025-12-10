package com.antigravity.ligam100.modules.event.service;

import com.antigravity.ligam100.modules.event.domain.Event;
import com.antigravity.ligam100.modules.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;

    public List<Event> findAll() {
        return repository.findAll();
    }

    public List<Event> findActive() {
        return repository.findByActivoTrue();
    }

    public Event create(Event event) {
        return repository.save(event);
    }

    public Event findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Evento no encontrado"));
    }
}
