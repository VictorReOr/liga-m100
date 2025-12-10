package com.antigravity.ligam100.modules.event.domain;

import com.antigravity.ligam100.core.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "disciplinas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discipline extends BaseEntity {

    @Column(nullable = false)
    private String nombre;

    @Column(name = "horario_inicio")
    private LocalDateTime horarioInicio;

    @Column(name = "horario_fin")
    private LocalDateTime horarioFin;

    @Column(name = "requiere_juez")
    @Builder.Default
    private boolean requiereJuez = true;

    @Column(name = "tipo_marca", nullable = false)
    private String tipoMarca; // TIEMPO, DISTANCIA, PUNTUACION

    // Configuración de reglas (JSON flexible)
    // Ejemplo: { "type": "POSITION", "scores": [10, 8, 6, 5, 4, 3, 2, 1] }
    @Column(name = "reglas_config_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> reglasConfig;

    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Event evento;
}
