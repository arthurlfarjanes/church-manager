package br.com.churchmanager.modules.missa.entity;

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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "intencao_missa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntencaoMissa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "horario_missa_id", nullable = false)
    private HorarioMissa horarioMissa;

    @Column(name = "data_celebracao", nullable = false)
    private LocalDate dataCelebracao;

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String tipo = "FALECIMENTO";

    @Column(name = "para_quem", nullable = false, length = 150)
    private String paraQuem;

    @Column(length = 150)
    private String solicitante;

    @Column(name = "valor_estipendio", precision = 10, scale = 2)
    private BigDecimal valorEstipendio;
}
