package com.antigravity.ligam100.modules.province.repository;

import com.antigravity.ligam100.modules.province.domain.Cpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpaRepository extends JpaRepository<Cpa, Long> {
    List<Cpa> findByProvinciaId(Long provinciaId);
}
