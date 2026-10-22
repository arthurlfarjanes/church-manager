package br.com.churchmanager.modules.paroquia.entity;

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

@Entity
@Table(name = "comunidade")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comunidade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paroquia_id", nullable = false)
    private Paroquia paroquia;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 255)
    private String endereco;

    @Column(name = "is_matriz", nullable = false)
    @Builder.Default
    private Boolean isMatriz = false;
}
