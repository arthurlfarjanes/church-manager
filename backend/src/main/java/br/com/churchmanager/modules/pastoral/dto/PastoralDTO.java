package br.com.churchmanager.modules.pastoral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class PastoralDTO {

    public record Request(
            @NotBlank(message = "O nome da pastoral/movimento é obrigatório")
            @Size(max = 150)
            String nome,

            String descricao,

            UUID coordenadorId,

            UUID viceCoordenadorId,

            UUID comunidadeId,

            Boolean ativo
    ) {}

    public record Response(
            UUID id,
            String nome,
            String descricao,
            UUID coordenadorId,
            String coordenadorNome,
            UUID viceCoordenadorId,
            String viceCoordenadorNome,
            UUID comunidadeId,
            String comunidadeNome,
            Boolean ativo,
            long totalMembros,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
