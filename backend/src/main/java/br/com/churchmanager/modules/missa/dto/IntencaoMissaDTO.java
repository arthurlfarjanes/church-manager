package br.com.churchmanager.modules.missa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public class IntencaoMissaDTO {

    public record Request(
            @NotNull(message = "O horário da missa é obrigatório")
            UUID horarioMissaId,

            @NotNull(message = "A data da celebração é obrigatória")
            LocalDate dataCelebracao,

            @Size(max = 50)
            String tipo,

            @NotBlank(message = "O destinatário da intenção é obrigatório")
            @Size(max = 150)
            String paraQuem,

            @Size(max = 150)
            String solicitante,

            BigDecimal valorEstipendio
    ) {}

    public record Response(
            UUID id,
            UUID horarioMissaId,
            String comunidadeNome,
            LocalTime horario,
            LocalDate dataCelebracao,
            String tipo,
            String paraQuem,
            String solicitante,
            BigDecimal valorEstipendio,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
