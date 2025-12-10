package com.antigravity.ligam100.modules.scoring.service;

import com.antigravity.ligam100.modules.province.domain.Cpa;
import com.antigravity.ligam100.modules.province.domain.Province;
import com.antigravity.ligam100.modules.province.repository.CpaRepository;
import com.antigravity.ligam100.modules.province.repository.ProvinceRepository;
import com.antigravity.ligam100.modules.scoring.domain.Score;
import com.antigravity.ligam100.modules.scoring.dto.RankingDto;
import com.antigravity.ligam100.modules.scoring.repository.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final ScoreRepository scoreRepository;
    private final ProvinceRepository provinceRepository;
    private final CpaRepository cpaRepository;

    public List<RankingDto.ProvinceRankingItem> getProvinceRanking(Long eventId) { // If eventId null, global ranking
        List<Score> scores;
        if (eventId != null) {
            scores = scoreRepository.findByEventoId(eventId);
        } else {
            scores = scoreRepository.findAll();
        }

        Map<Long, Integer> pointsMap = new HashMap<>();

        for (Score s : scores) {
            if (s.getCalculoPuntos() != null) {
                Long provId = s.getInscripcion().getDeportista().getCpa().getProvincia().getId();
                pointsMap.put(provId, pointsMap.getOrDefault(provId, 0) + s.getCalculoPuntos());
            }
        }

        List<RankingDto.ProvinceRankingItem> ranking = new ArrayList<>();
        List<Province> provinces = provinceRepository.findAll();

        for (Province p : provinces) {
            ranking.add(RankingDto.ProvinceRankingItem.builder()
                    .provinceId(p.getId())
                    .provinceName(p.getNombre())
                    .totalPoints(pointsMap.getOrDefault(p.getId(), 0))
                    .build());
        }

        ranking.sort(Comparator.comparingInt(RankingDto.ProvinceRankingItem::getTotalPoints).reversed());
        return ranking;
    }

    public List<RankingDto.CpaRankingItem> getCpaRanking(Long eventId) {
        List<Score> scores;
        if (eventId != null) {
            scores = scoreRepository.findByEventoId(eventId);
        } else {
            scores = scoreRepository.findAll();
        }

        Map<Long, Integer> pointsMap = new HashMap<>();

        for (Score s : scores) {
            if (s.getCalculoPuntos() != null) {
                Long cpaId = s.getInscripcion().getDeportista().getCpa().getId();
                pointsMap.put(cpaId, pointsMap.getOrDefault(cpaId, 0) + s.getCalculoPuntos());
            }
        }

        List<RankingDto.CpaRankingItem> ranking = new ArrayList<>();
        List<Cpa> cpas = cpaRepository.findAll();

        for (Cpa c : cpas) {
            ranking.add(RankingDto.CpaRankingItem.builder()
                    .cpaId(c.getId())
                    .cpaName(c.getNombre())
                    .provinceName(c.getProvincia().getNombre())
                    .totalPoints(pointsMap.getOrDefault(c.getId(), 0))
                    .build());
        }

        ranking.sort(Comparator.comparingInt(RankingDto.CpaRankingItem::getTotalPoints).reversed());
        return ranking;
    }
}
