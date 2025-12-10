package com.antigravity.ligam100.modules.athlete.service;

import com.antigravity.ligam100.modules.athlete.domain.Athlete;
import com.antigravity.ligam100.modules.athlete.repository.AthleteRepository;
import com.antigravity.ligam100.modules.province.domain.Cpa;
import com.antigravity.ligam100.modules.province.repository.CpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AthleteService {

    private final AthleteRepository repository;
    private final CpaRepository cpaRepository;

    public List<Athlete> findAll() {
        return repository.findAll();
    }

    public List<Athlete> findByCpa(Long cpaId) {
        return repository.findByCpaId(cpaId);
    }

    public Athlete create(CreateAthleteRequest request) {
        // Validar DNI único si existe
        if (request.dni() != null && !request.dni().isEmpty()) {
            if (repository.findByDni(request.dni()).isPresent()) {
                throw new RuntimeException("Ya existe un deportista con el DNI: " + request.dni());
            }
        }

        Cpa cpa = cpaRepository.findById(request.cpaId())
                .orElseThrow(() -> new RuntimeException("CPA no encontrado"));

        Athlete athlete = Athlete.builder()
                .nombre(request.nombre())
                .dni(request.dni())
                .anioNacimiento(request.anioNacimiento())
                .genero(request.genero())
                .cpa(cpa)
                .build();

        return repository.save(athlete);
    }
    
    public record CreateAthleteRequest(String nombre, String dni, Integer anioNacimiento, String genero, Long cpaId) {}
}
