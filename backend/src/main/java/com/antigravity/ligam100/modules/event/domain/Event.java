package com.antigravity.ligam100.modules.event.domain;

import com.antigravity.ligam100.core.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "eventos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tipo; // CLASIFICATORIO, FINAL

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "max_pruebas_por_deportista")
    @Builder.Default
    private Integer maxPruebasPorDeportista = 3;

    @Builder.Default
    private boolean activo = true;

    // Almacenamos los IDs de provincias participantes como JSON array simple
    @Column(name = "provincias_participantes_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> provinciasParticipantes; 
}
