package br.com.churchmanager.modules.pastoral.controller;

import br.com.churchmanager.modules.pastoral.dto.AtividadePastoralDTO;
import br.com.churchmanager.modules.pastoral.dto.PastoralDTO;
import br.com.churchmanager.modules.pastoral.dto.PastoralMembroDTO;
import br.com.churchmanager.modules.pastoral.service.PastoralService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pastorais")
@RequiredArgsConstructor
@Tag(name = "Pastorais e Movimentos", description = "Endpoints para gerenciamento de pastorais, membros e atividades")
public class PastoralController {

    private final PastoralService pastoralService;

    // --- Pastorais ---
    @GetMapping
    @Operation(summary = "Listar pastorais com filtros opcionais")
    public ResponseEntity<List<PastoralDTO.Response>> listar(
            @RequestParam(required = false) UUID comunidadeId,
            @RequestParam(required = false) Boolean apenasAtivos,
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(pastoralService.listar(comunidadeId, apenasAtivos, busca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pastoral por ID")
    public ResponseEntity<PastoralDTO.Response> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pastoralService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova pastoral ou movimento")
    public ResponseEntity<PastoralDTO.Response> criar(@Valid @RequestBody PastoralDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pastoralService.criar(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pastoral")
    public ResponseEntity<PastoralDTO.Response> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PastoralDTO.Request req) {
        return ResponseEntity.ok(pastoralService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover pastoral")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        pastoralService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Membros ---
    @GetMapping("/{id}/membros")
    @Operation(summary = "Listar membros de uma pastoral")
    public ResponseEntity<List<PastoralMembroDTO.Response>> listarMembros(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "false") Boolean apenasAtivos) {
        return ResponseEntity.ok(pastoralService.listarMembros(id, apenasAtivos));
    }

    @PostMapping("/membros")
    @Operation(summary = "Adicionar membro a uma pastoral")
    public ResponseEntity<PastoralMembroDTO.Response> adicionarMembro(@Valid @RequestBody PastoralMembroDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pastoralService.adicionarMembro(req));
    }

    @PutMapping("/membros/{membroId}")
    @Operation(summary = "Atualizar função ou status de um membro da pastoral")
    public ResponseEntity<PastoralMembroDTO.Response> atualizarMembro(
            @PathVariable UUID membroId,
            @Valid @RequestBody PastoralMembroDTO.UpdateRequest req) {
        return ResponseEntity.ok(pastoralService.atualizarMembro(membroId, req));
    }

    @DeleteMapping("/membros/{membroId}")
    @Operation(summary = "Remover membro da pastoral")
    public ResponseEntity<Void> removerMembro(@PathVariable UUID membroId) {
        pastoralService.removerMembro(membroId);
        return ResponseEntity.noContent().build();
    }

    // --- Atividades ---
    @GetMapping("/{id}/atividades")
    @Operation(summary = "Listar atividades da pastoral em um período")
    public ResponseEntity<List<AtividadePastoralDTO.Response>> listarAtividades(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fim) {
        return ResponseEntity.ok(pastoralService.listarAtividades(id, inicio, fim));
    }

    @PostMapping("/atividades")
    @Operation(summary = "Cadastrar nova atividade pastoral")
    public ResponseEntity<AtividadePastoralDTO.Response> criarAtividade(@Valid @RequestBody AtividadePastoralDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pastoralService.criarAtividade(req));
    }

    @PutMapping("/atividades/{id}")
    @Operation(summary = "Atualizar atividade pastoral")
    public ResponseEntity<AtividadePastoralDTO.Response> atualizarAtividade(
            @PathVariable UUID id,
            @Valid @RequestBody AtividadePastoralDTO.Request req) {
        return ResponseEntity.ok(pastoralService.atualizarAtividade(id, req));
    }

    @DeleteMapping("/atividades/{id}")
    @Operation(summary = "Remover atividade pastoral")
    public ResponseEntity<Void> deletarAtividade(@PathVariable UUID id) {
        pastoralService.deletarAtividade(id);
        return ResponseEntity.noContent().build();
    }
}
