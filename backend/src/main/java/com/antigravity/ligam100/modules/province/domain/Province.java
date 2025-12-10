package com.antigravity.ligam100.modules.province.domain;

import com.antigravity.ligam100.core.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "provincias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Province extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nombre;
}
