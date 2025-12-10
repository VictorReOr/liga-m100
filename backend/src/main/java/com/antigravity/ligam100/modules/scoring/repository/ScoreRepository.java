package com.antigravity.ligam100.modules.scoring.repository;

import com.antigravity.ligam100.modules.scoring.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    
    Optional<Score> findByInscripcionId(Long inscripcionId);
    
    @Query("SELECT s FROM Score s WHERE s.inscripcion.evento.id = :eventoId")
    List<Score> findByEventoId(@Param("eventoId") Long eventoId);

    @Query("SELECT s FROM Score s WHERE s.inscripcion.disciplina.id = :disciplinaId")
    List<Score> findByDisciplinaId(@Param("disciplinaId") Long disciplinaId);
    
    List<Score> findByJuezId(Long juezId);
}
