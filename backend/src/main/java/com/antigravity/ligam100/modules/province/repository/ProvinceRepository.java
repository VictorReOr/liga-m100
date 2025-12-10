package com.antigravity.ligam100.modules.province.repository;

import com.antigravity.ligam100.modules.province.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Optional<Province> findByNombre(String nombre);
}
