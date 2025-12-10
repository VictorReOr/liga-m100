package com.antigravity.ligam100.modules.athlete.repository;

import com.antigravity.ligam100.modules.athlete.domain.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    List<Athlete> findByCpaId(Long cpaId);
    Optional<Athlete> findByDni(String dni);
}
