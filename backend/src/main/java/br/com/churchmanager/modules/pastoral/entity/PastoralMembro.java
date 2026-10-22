package br.com.churchmanager.modules.pastoral.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
    name = "pastoral_membro",
    uniqueConstraints = @UniqueConstraint(name = "uk_pastoral_membro", columnNames = {"pastoral_id", "pessoa_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PastoralMembro extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pastoral_id", nullable = false)
    private Pastoral pastoral;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @Column(length = 50)
    @Builder.Default
    private String funcao = "AGENTE";

    @Column(name = "data_ingresso")
    @Builder.Default
    private LocalDate dataIngresso = LocalDate.now();

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}
