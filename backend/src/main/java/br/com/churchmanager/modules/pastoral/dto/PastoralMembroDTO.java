package br.com.churchmanager.modules.pastoral.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class PastoralMembroDTO {

    public record Request(
            @NotNull(message = "A pastoral é obrigatória")
            UUID pastoralId,

            @NotNull(message = "A pessoa é obrigatória")
            UUID pessoaId,

            @Size(max = 50)
            String funcao,

            LocalDate dataIngresso,

            Boolean ativo
    ) {}

    public record UpdateRequest(
            @Size(max = 50)
            String funcao,

            Boolean ativo
    ) {}

    public record Response(
            UUID id,
            UUID pastoralId,
            String pastoralNome,
            UUID pessoaId,
            String pessoaNome,
            String pessoaTelefone,
            String pessoaEmail,
            String funcao,
            LocalDate dataIngresso,
            Boolean ativo,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
