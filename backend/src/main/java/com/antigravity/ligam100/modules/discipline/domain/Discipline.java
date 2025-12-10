package com.antigravity.ligam100.modules.discipline.domain;

import com.antigravity.ligam100.core.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

/**
 * Represents a sports discipline.
 * <p>
 * Contains configuration for scoring rules in JSON format.
 * </p>
 *
 * @author Antigravity
 * @since 2025-12-08
 */
@Entity
@Table(name = "disciplines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(name = "has_judge")
    @Builder.Default
    private Boolean hasJudge = true;

    @Column(name = "scoring_config", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> scoringConfig;
}
