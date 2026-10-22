package br.com.churchmanager.modules.missa.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "horario_missa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioMissa extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comunidade_id", nullable = false)
    private Comunidade comunidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false, length = 20)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private LocalTime horario;

    @Column(length = 100)
    private String celebrante;

    @Column(length = 150)
    private String descricao;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Builder.Default
    @OneToMany(mappedBy = "horarioMissa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IntencaoMissa> intencoes = new ArrayList<>();
}
