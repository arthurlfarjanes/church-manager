package br.com.churchmanager.modules.inventario.controller;

import br.com.churchmanager.modules.inventario.dto.CategoriaItemDTO;
import br.com.churchmanager.modules.inventario.dto.ItemPatrimonioDTO;
import br.com.churchmanager.modules.inventario.dto.MovimentacaoItemDTO;
import br.com.churchmanager.modules.inventario.entity.StatusItem;
import br.com.churchmanager.modules.inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Tag(name = "Inventário e Patrimônio", description = "Endpoints para gerenciamento do patrimônio paroquial e movimentações")
public class InventarioController {

    private final InventarioService inventarioService;

    // --- Categorias ---
    @GetMapping("/categorias")
    @Operation(summary = "Listar todas as categorias de itens")
    public ResponseEntity<List<CategoriaItemDTO.Response>> listarCategorias() {
        return ResponseEntity.ok(inventarioService.listarCategorias());
    }

    @GetMapping("/categorias/{id}")
    @Operation(summary = "Buscar categoria por ID")
    public ResponseEntity<CategoriaItemDTO.Response> buscarCategoriaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(inventarioService.buscarCategoriaPorId(id));
    }

    @PostMapping("/categorias")
    @Operation(summary = "Criar nova categoria de patrimônio")
    public ResponseEntity<CategoriaItemDTO.Response> criarCategoria(@Valid @RequestBody CategoriaItemDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.criarCategoria(req));
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "Atualizar categoria de patrimônio")
    public ResponseEntity<CategoriaItemDTO.Response> atualizarCategoria(
            @PathVariable UUID id,
            @Valid @RequestBody CategoriaItemDTO.Request req) {
        return ResponseEntity.ok(inventarioService.atualizarCategoria(id, req));
    }

    @DeleteMapping("/categorias/{id}")
    @Operation(summary = "Remover categoria de patrimônio")
    public ResponseEntity<Void> deletarCategoria(@PathVariable UUID id) {
        inventarioService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    // --- Itens de Patrimônio ---
    @GetMapping("/itens")
    @Operation(summary = "Listar itens de patrimônio com filtros opcionais")
    public ResponseEntity<List<ItemPatrimonioDTO.Response>> listarItens(
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(required = false) UUID comunidadeId,
            @RequestParam(required = false) StatusItem status,
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(inventarioService.listarItens(categoriaId, comunidadeId, status, busca));
    }

    @GetMapping("/itens/{id}")
    @Operation(summary = "Buscar item de patrimônio por ID")
    public ResponseEntity<ItemPatrimonioDTO.Response> buscarItemPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(inventarioService.buscarItemPorId(id));
    }

    @PostMapping("/itens")
    @Operation(summary = "Cadastrar novo item no patrimônio")
    public ResponseEntity<ItemPatrimonioDTO.Response> criarItem(@Valid @RequestBody ItemPatrimonioDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.criarItem(req));
    }

    @PutMapping("/itens/{id}")
    @Operation(summary = "Atualizar item de patrimônio")
    public ResponseEntity<ItemPatrimonioDTO.Response> atualizarItem(
            @PathVariable UUID id,
            @Valid @RequestBody ItemPatrimonioDTO.Request req) {
        return ResponseEntity.ok(inventarioService.atualizarItem(id, req));
    }

    @DeleteMapping("/itens/{id}")
    @Operation(summary = "Remover item de patrimônio")
    public ResponseEntity<Void> deletarItem(@PathVariable UUID id) {
        inventarioService.deletarItem(id);
        return ResponseEntity.noContent().build();
    }

    // --- Movimentações ---
    @GetMapping("/itens/{id}/movimentacoes")
    @Operation(summary = "Histórico de movimentações de um item")
    public ResponseEntity<List<MovimentacaoItemDTO.Response>> listarMovimentacoes(@PathVariable UUID id) {
        return ResponseEntity.ok(inventarioService.listarMovimentacoesPorItem(id));
    }

    @PostMapping("/movimentacoes")
    @Operation(summary = "Registrar movimentação (empréstimo, devolução, transferência, manutenção, baixa)")
    public ResponseEntity<MovimentacaoItemDTO.Response> registrarMovimentacao(
            @Valid @RequestBody MovimentacaoItemDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.registrarMovimentacao(req));
    }
}
