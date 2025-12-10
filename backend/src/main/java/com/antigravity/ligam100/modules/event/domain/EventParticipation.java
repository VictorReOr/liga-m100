package com.antigravity.ligam100.modules.event.domain;

import com.antigravity.ligam100.core.BaseEntity;
import com.antigravity.ligam100.modules.province.domain.Cpa;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Represents a CPA's participation in a specific event.
 *
 * @author Antigravity
 * @since 2025-12-08
 */
@Entity
@Table(name = "event_participations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventParticipation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpa_id", nullable = false)
    private Cpa cpa;
}
