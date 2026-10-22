package br.com.churchmanager.modules.paroquia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class ParoquiaDTO {

    public record Request(
            @NotBlank(message = "O nome da paróquia é obrigatório")
            @Size(max = 150)
            String nome,

            @Size(max = 150)
            String diocese,

            @Size(max = 150)
            String paroco,

            @Size(max = 30)
            String telefone,

            @Size(max = 100)
            String email,

            @Size(max = 255)
            String endereco,

            @Size(max = 100)
            String cidade,

            @Size(max = 2)
            String estado
    ) {}

    public record Response(
            UUID id,
            String nome,
            String diocese,
            String paroco,
            String telefone,
            String email,
            String endereco,
            String cidade,
            String estado,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
