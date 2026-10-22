package br.com.churchmanager.modules.inventario.dto;

import br.com.churchmanager.modules.inventario.entity.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MovimentacaoItemDTO {

    public record Request(
            @NotNull(message = "O ID do item é obrigatório")
            UUID itemId,

            @NotNull(message = "O tipo de movimentação é obrigatório")
            TipoMovimentacao tipo,

            @Size(max = 150)
            String origem,

            @Size(max = 150)
            String destino,

            UUID responsavelId,

            String observacoes
    ) {}

    public record Response(
            UUID id,
            UUID itemId,
            String itemNome,
            TipoMovimentacao tipo,
            OffsetDateTime dataMovimentacao,
            String origem,
            String destino,
            UUID responsavelId,
            String responsavelNome,
            String observacoes,
            OffsetDateTime createdAt
    ) {}
}
