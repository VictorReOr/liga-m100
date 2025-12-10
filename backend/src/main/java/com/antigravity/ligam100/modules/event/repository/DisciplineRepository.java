package com.antigravity.ligam100.modules.event.repository;

import com.antigravity.ligam100.modules.event.domain.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    List<Discipline> findByEventoId(Long eventoId);
}
