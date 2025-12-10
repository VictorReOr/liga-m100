package com.antigravity.ligam100.modules.athlete.domain;

import com.antigravity.ligam100.core.BaseEntity;
import com.antigravity.ligam100.modules.province.domain.Cpa;
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
@Table(name = "deportistas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Athlete extends BaseEntity {

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true)
    private String dni;

    @Column(name = "anio_nacimiento", nullable = false)
    private Integer anioNacimiento;

    @Column(nullable = false)
    private String genero; // MASCULINO, FEMENINO

    @ManyToOne
    @JoinColumn(name = "cpa_id", nullable = false)
    private Cpa cpa;
}
