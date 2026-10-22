package br.com.churchmanager.modules.paroquia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ComunidadeDTO {

    public record Request(
            @NotNull(message = "O ID da paróquia é obrigatório")
            UUID paroquiaId,

            @NotBlank(message = "O nome da comunidade/capela é obrigatório")
            @Size(max = 150)
            String nome,

            @Size(max = 255)
            String endereco,

            Boolean isMatriz
    ) {}

    public record Response(
            UUID id,
            UUID paroquiaId,
            String paroquiaNome,
            String nome,
            String endereco,
            Boolean isMatriz,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
