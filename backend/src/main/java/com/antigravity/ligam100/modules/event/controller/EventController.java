package com.antigravity.ligam100.modules.event.controller;

import com.antigravity.ligam100.modules.event.domain.Discipline;
import com.antigravity.ligam100.modules.event.domain.Event;
import com.antigravity.ligam100.modules.event.service.DisciplineService;
import com.antigravity.ligam100.modules.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final DisciplineService disciplineService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.create(event));
    }

    @GetMapping("/{eventId}/disciplines")
    public ResponseEntity<List<Discipline>> getDisciplinesByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(disciplineService.findByEvent(eventId));
    }

    @PostMapping("/{eventId}/disciplines")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Discipline> createDiscipline(
            @PathVariable Long eventId,
            @RequestBody CreateDisciplineRequest request
    ) {
        Discipline discipline = Discipline.builder()
                .nombre(request.nombre())
                .horarioInicio(request.horarioInicio())
                .horarioFin(request.horarioFin())
                .requiereJuez(request.requiereJuez())
                .tipoMarca(request.tipoMarca())
                .reglasConfig(request.reglasConfig())
                .build();
        return ResponseEntity.ok(disciplineService.create(discipline, eventId));
    }

    private final com.antigravity.ligam100.modules.event.service.ExcelImportService excelImportService;

    @PostMapping(value = "/{eventId}/import-schedules", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> importSchedules(
            @PathVariable Long eventId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file
    ) {
        excelImportService.importSchedules(eventId, file);
        return ResponseEntity.ok().build();
    }

    public record CreateDisciplineRequest(
            String nombre,
            LocalDateTime horarioInicio,
            LocalDateTime horarioFin,
            boolean requiereJuez,
            String tipoMarca,
            Map<String, Object> reglasConfig
    ) {}
}
