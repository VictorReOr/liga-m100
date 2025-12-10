package com.antigravity.ligam100.modules.province.controller;

import com.antigravity.ligam100.modules.province.domain.Cpa;
import com.antigravity.ligam100.modules.province.domain.Province;
import com.antigravity.ligam100.modules.province.service.CpaService;
import com.antigravity.ligam100.modules.province.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/master-data")
@RequiredArgsConstructor
public class MasterDataController {

    private final ProvinceService provinceService;
    private final CpaService cpaService;

    @GetMapping("/provinces")
    public ResponseEntity<List<Province>> getAllProvinces() {
        return ResponseEntity.ok(provinceService.findAll());
    }

    @PostMapping("/provinces")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Province> createProvince(@RequestBody Province province) {
        return ResponseEntity.ok(provinceService.create(province));
    }

    @GetMapping("/cpas")
    public ResponseEntity<List<Cpa>> getAllCpas(@RequestParam(required = false) Long provinceId) {
        if (provinceId != null) {
            return ResponseEntity.ok(cpaService.findByProvince(provinceId));
        }
        return ResponseEntity.ok(cpaService.findAll());
    }

    @PostMapping("/cpas")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Cpa> createCpa(@RequestBody CreateCpaRequest request) {
        return ResponseEntity.ok(cpaService.create(request.nombre(), request.provinciaId()));
    }

    public record CreateCpaRequest(String nombre, Long provinciaId) {}
}
