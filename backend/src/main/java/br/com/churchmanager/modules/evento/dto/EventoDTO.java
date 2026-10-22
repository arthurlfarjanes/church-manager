package br.com.churchmanager.modules.evento.dto;

import br.com.churchmanager.modules.evento.entity.StatusEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class EventoDTO {

    public record Request(
            @NotBlank(message = "O título do evento é obrigatório")
            @Size(max = 150)
            String titulo,

            String descricao,

            UUID comunidadeId,

            @Size(max = 150)
            String localEspaco,

            @NotNull(message = "A data e hora de início são obrigatórias")
            OffsetDateTime dataInicio,

            OffsetDateTime dataFim,

            @Size(max = 50)
            String tipo,

            StatusEvento status,

            UUID responsavelId
    ) {}

    public record Response(
            UUID id,
            String titulo,
            String descricao,
            UUID comunidadeId,
            String comunidadeNome,
            String localEspaco,
            OffsetDateTime dataInicio,
            OffsetDateTime dataFim,
            String tipo,
            StatusEvento status,
            UUID responsavelId,
            String responsavelNome,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
