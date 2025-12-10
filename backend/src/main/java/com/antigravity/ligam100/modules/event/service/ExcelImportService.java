package com.antigravity.ligam100.modules.event.service;

import com.antigravity.ligam100.modules.event.domain.Discipline;
import com.antigravity.ligam100.modules.event.domain.Event;
import com.antigravity.ligam100.modules.event.repository.DisciplineRepository;
import com.antigravity.ligam100.modules.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final DisciplineRepository disciplineRepository;
    private final EventRepository eventRepository;

    public void importSchedules(Long eventId, MultipartFile file) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Discipline> disciplinesToUpdate = new ArrayList<>();

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                // Columnas esperadas: 
                // 0: Nombre Disciplina (debe coincidir exactamente)
                // 1: Fecha Inicio (YYYY-MM-DD HH:mm)
                // 2: Fecha Fin (YYYY-MM-DD HH:mm)

                String disciplineName = getCellValue(row.getCell(0));
                String startDateStr = getCellValue(row.getCell(1));
                String endDateStr = getCellValue(row.getCell(2));

                if (disciplineName == null || disciplineName.isEmpty()) continue;

                // Buscar disciplina en el evento por nombre
                List<Discipline> eventDisciplines = disciplineRepository.findByEventoId(eventId);
                Discipline discipline = eventDisciplines.stream()
                        .filter(d -> d.getNombre().equalsIgnoreCase(disciplineName))
                        .findFirst()
                        .orElse(null);

                if (discipline != null && startDateStr != null && endDateStr != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime start = LocalDateTime.parse(startDateStr, formatter);
                    LocalDateTime end = LocalDateTime.parse(endDateStr, formatter);

                    discipline.setHorarioInicio(start);
                    discipline.setHorarioFin(end);
                    disciplinesToUpdate.add(discipline);
                }
            }

            disciplineRepository.saveAll(disciplinesToUpdate);

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar el archivo Excel: " + e.getMessage());
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf(cell.getNumericCellValue());
        return null;
    }
}
