package com.antigravity.ligam100.modules.event.domain;

import com.antigravity.ligam100.core.BaseEntity;
import com.antigravity.ligam100.modules.athlete.domain.Athlete;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inscripciones", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"evento_id", "deportista_id", "disciplina_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Event evento;

    @ManyToOne
    @JoinColumn(name = "deportista_id", nullable = false)
    private Athlete deportista;

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Discipline disciplina;
}
