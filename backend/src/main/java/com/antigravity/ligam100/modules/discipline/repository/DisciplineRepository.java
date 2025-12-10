package com.antigravity.ligam100.modules.discipline.repository;

import com.antigravity.ligam100.modules.discipline.domain.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DisciplineRepository extends JpaRepository<Discipline, UUID> {
}
