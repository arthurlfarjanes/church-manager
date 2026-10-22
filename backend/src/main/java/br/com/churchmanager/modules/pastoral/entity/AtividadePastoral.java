package br.com.churchmanager.modules.pastoral.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "atividade_pastoral")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtividadePastoral extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pastoral_id", nullable = false)
    private Pastoral pastoral;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_hora", nullable = false)
    private OffsetDateTime dataHora;

    @Column(length = 150)
    private String local;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "AGENDADA";
}
