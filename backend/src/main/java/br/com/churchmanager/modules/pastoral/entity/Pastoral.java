package br.com.churchmanager.modules.pastoral.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pastoral")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pastoral extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coordenador_id")
    private Pessoa coordenador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vice_coordenador_id")
    private Pessoa viceCoordenador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comunidade_id")
    private Comunidade comunidade;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Builder.Default
    @OneToMany(mappedBy = "pastoral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PastoralMembro> membros = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pastoral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AtividadePastoral> atividades = new ArrayList<>();
}
