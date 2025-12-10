package com.antigravity.ligam100.modules.scoring.domain;

import com.antigravity.ligam100.core.BaseEntity;
import com.antigravity.ligam100.modules.auth.domain.User;
import com.antigravity.ligam100.modules.event.domain.Registration;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "puntuaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Score extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "inscripcion_id", nullable = false)
    private Registration inscripcion;

    @ManyToOne
    @JoinColumn(name = "juez_id")
    private User juez;

    @Column(precision = 10, scale = 3)
    private BigDecimal marca; // Valor numérico

    @Column(name = "marca_texto")
    private String marcaTexto;

    // Para guardar intentos múltiples si fuera necesario
    @Column(name = "intentos", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> intentos;

    @Column(name = "es_valida")
    @Builder.Default
    private boolean esValida = true;

    private String notas;

    @Column(name = "calculo_puntos")
    private Integer calculoPuntos; // Puntos calculados (10, 8, etc.)
}
