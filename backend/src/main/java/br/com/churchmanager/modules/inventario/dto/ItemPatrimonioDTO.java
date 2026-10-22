package br.com.churchmanager.modules.inventario.dto;

import br.com.churchmanager.modules.inventario.entity.EstadoConservacao;
import br.com.churchmanager.modules.inventario.entity.StatusItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class ItemPatrimonioDTO {

    public record Request(
            @Size(max = 50)
            String codigoTombamento,

            @NotBlank(message = "O nome do item é obrigatório")
            @Size(max = 150)
            String nome,

            String descricao,

            @NotNull(message = "A categoria é obrigatória")
            UUID categoriaId,

            UUID comunidadeId,

            @Size(max = 100)
            String localizacao,

            @NotNull(message = "A quantidade é obrigatória")
            @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
            Integer quantidade,

            BigDecimal valorEstimado,

            EstadoConservacao estadoConservacao,

            StatusItem status,

            LocalDate dataAquisicao
    ) {}

    public record Response(
            UUID id,
            String codigoTombamento,
            String nome,
            String descricao,
            UUID categoriaId,
            String categoriaNome,
            UUID comunidadeId,
            String comunidadeNome,
            String localizacao,
            Integer quantidade,
            BigDecimal valorEstimado,
            EstadoConservacao estadoConservacao,
            StatusItem status,
            LocalDate dataAquisicao,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}
