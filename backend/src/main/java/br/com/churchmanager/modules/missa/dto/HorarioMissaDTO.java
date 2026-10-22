package br.com.churchmanager.modules.missa.dto;

import br.com.churchmanager.modules.missa.entity.DiaSemana;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class HorarioMissaDTO {

    public record Request(
            @NotNull(message = "A comunidade é obrigatória")
            UUID comunidadeId,

            @NotNull(message = "O dia da semana é obrigatório")
            DiaSemana diaSemana,

            @NotNull(message = "O horário é obrigatório")
            LocalTime horario,

            @Size(max = 100)
            String celebrante,

            @Size(max = 150)
            String descricao,

            Boolean ativo
    ) {}

    public record Response(
            UUID id,
            UUID comunidadeId,
            String comunidadeNome,
            DiaSemana diaSemana,
            LocalTime horario,
            String celebrante,
            String descricao,
            Boolean ativo,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
