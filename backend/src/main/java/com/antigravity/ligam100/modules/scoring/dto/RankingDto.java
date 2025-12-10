package com.antigravity.ligam100.modules.scoring.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RankingDto {

    @Data
    @Builder
    public static class ProvinceRankingItem {
        private Long provinceId;
        private String provinceName;
        private Integer totalPoints;
    }

    @Data
    @Builder
    public static class CpaRankingItem {
        private Long cpaId;
        private String cpaName;
        private String provinceName;
        private Integer totalPoints;
    }
}
