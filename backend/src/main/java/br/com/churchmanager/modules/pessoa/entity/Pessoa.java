package br.com.churchmanager.modules.pessoa.entity;

import br.com.churchmanager.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 100)
    private String email;

    @Column(length = 30)
    private String telefone;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(length = 255)
    private String endereco;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
