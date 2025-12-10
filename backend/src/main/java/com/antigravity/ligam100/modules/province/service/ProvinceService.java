package com.antigravity.ligam100.modules.province.service;

import com.antigravity.ligam100.modules.province.domain.Province;
import com.antigravity.ligam100.modules.province.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository repository;

    public List<Province> findAll() {
        return repository.findAll();
    }

    public Province create(Province province) {
        // En un caso real validaríamos duplicados, etc.
        return repository.save(province);
    }
    
    public Province findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Provincia no encontrada"));
    }
}
