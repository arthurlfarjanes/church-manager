package br.com.churchmanager.modules.paroquia.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "paroquia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paroquia extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 150)
    private String diocese;

    @Column(length = 150)
    private String paroco;

    @Column(length = 30)
    private String telefone;

    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(length = 100)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Builder.Default
    @OneToMany(mappedBy = "paroquia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comunidade> comunidades = new ArrayList<>();
}
