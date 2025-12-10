package com.antigravity.ligam100.modules.province.domain;

import com.antigravity.ligam100.core.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "cpas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cpa extends BaseEntity {

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "provincia_id", nullable = false)
    private Province provincia;
}
