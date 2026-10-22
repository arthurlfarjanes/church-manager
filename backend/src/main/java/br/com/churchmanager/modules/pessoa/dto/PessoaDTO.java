package br.com.churchmanager.modules.pessoa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PessoaDTO {

    public record Request(
            @NotBlank(message = "O nome é obrigatório")
            @Size(max = 150)
            String nome,

            @Email(message = "Formato de e-mail inválido")
            @Size(max = 100)
            String email,

            @Size(max = 30)
            String telefone,

            LocalDate dataNascimento,

            @Size(max = 255)
            String endereco,

            String observacoes
    ) {}

    public record Response(
            UUID id,
            String nome,
            String email,
            String telefone,
            LocalDate dataNascimento,
            String endereco,
            String observacoes,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
