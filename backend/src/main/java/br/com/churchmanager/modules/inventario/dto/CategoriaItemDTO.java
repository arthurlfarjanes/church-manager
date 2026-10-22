package br.com.churchmanager.modules.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CategoriaItemDTO {

    public record Request(
            @NotBlank(message = "O nome da categoria é obrigatório")
            @Size(max = 100)
            String nome,

            String descricao
    ) {}

    public record Response(
            UUID id,
            String nome,
            String descricao,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
