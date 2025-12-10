package com.antigravity.ligam100.modules.scoring.controller;

import com.antigravity.ligam100.modules.scoring.dto.RankingDto;
import com.antigravity.ligam100.modules.scoring.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService service;

    @GetMapping("/provinces")
    public ResponseEntity<List<RankingDto.ProvinceRankingItem>> getProvinceRanking(@RequestParam(required = false) Long eventId) {
        return ResponseEntity.ok(service.getProvinceRanking(eventId));
    }

    @GetMapping("/cpas")
    public ResponseEntity<List<RankingDto.CpaRankingItem>> getCpaRanking(@RequestParam(required = false) Long eventId) {
        return ResponseEntity.ok(service.getCpaRanking(eventId));
    }
}
