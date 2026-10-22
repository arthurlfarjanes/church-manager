package br.com.churchmanager.modules.inventario.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "movimentacao_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemPatrimonio item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimentacao tipo;

    @Column(name = "data_movimentacao", nullable = false)
    @Builder.Default
    private OffsetDateTime dataMovimentacao = OffsetDateTime.now();

    @Column(length = 150)
    private String origem;

    @Column(length = 150)
    private String destino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Pessoa responsavel;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
