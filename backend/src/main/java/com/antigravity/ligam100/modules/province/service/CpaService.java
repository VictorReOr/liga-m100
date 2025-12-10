package com.antigravity.ligam100.modules.province.service;

import com.antigravity.ligam100.modules.province.domain.Cpa;
import com.antigravity.ligam100.modules.province.domain.Province;
import com.antigravity.ligam100.modules.province.repository.CpaRepository;
import com.antigravity.ligam100.modules.province.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CpaService {

    private final CpaRepository cpaRepository;
    private final ProvinceRepository provinceRepository;

    public List<Cpa> findAll() {
        return cpaRepository.findAll();
    }
    
    public List<Cpa> findByProvince(Long provinceId) {
        return cpaRepository.findByProvinciaId(provinceId);
    }

    public Cpa create(String nombre, Long provinceId) {
        Province province = provinceRepository.findById(provinceId)
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con ID: " + provinceId));
                
        Cpa cpa = Cpa.builder()
                .nombre(nombre)
                .provincia(province)
                .build();
                
        return cpaRepository.save(cpa);
    }
}
