package br.com.churchmanager.modules.pastoral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class AtividadePastoralDTO {

    public record Request(
            @NotNull(message = "A pastoral é obrigatória")
            UUID pastoralId,

            @NotBlank(message = "O título da atividade é obrigatório")
            @Size(max = 150)
            String titulo,

            String descricao,

            @NotNull(message = "A data e hora são obrigatórias")
            OffsetDateTime dataHora,

            @Size(max = 150)
            String local,

            @Size(max = 30)
            String status
    ) {}

    public record Response(
            UUID id,
            UUID pastoralId,
            String pastoralNome,
            String titulo,
            String descricao,
            OffsetDateTime dataHora,
            String local,
            String status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
