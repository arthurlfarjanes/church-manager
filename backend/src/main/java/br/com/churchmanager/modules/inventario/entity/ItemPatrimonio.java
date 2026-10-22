package br.com.churchmanager.modules.inventario.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "item_patrimonio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPatrimonio extends BaseEntity {

    @Column(name = "codigo_tombamento", unique = true, length = 50)
    private String codigoTombamento;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaItem categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;

    @Column(length = 100)
    private String localizacao;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantidade = 1;

    @Column(name = "valor_estimado", precision = 12, scale = 2)
    private BigDecimal valorEstimado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conservacao", nullable = false, length = 30)
    @Builder.Default
    private EstadoConservacao estadoConservacao = EstadoConservacao.BOM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatusItem status = StatusItem.DISPONIVEL;

    @Column(name = "data_aquisicao")
    private LocalDate dataAquisicao;
}
