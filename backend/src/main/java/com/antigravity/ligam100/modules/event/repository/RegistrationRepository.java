package com.antigravity.ligam100.modules.event.repository;

import com.antigravity.ligam100.modules.event.domain.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEventoIdAndDeportistaId(Long eventoId, Long deportistaId);
    List<Registration> findByEventoId(Long eventoId);
    
    // Para verificar si ya existe
    boolean existsByEventoIdAndDeportistaIdAndDisciplinaId(Long eventoId, Long deportistaId, Long disciplinaId);
}
