package br.com.churchmanager.modules.evento.controller;

import br.com.churchmanager.modules.evento.dto.EventoDTO;
import br.com.churchmanager.modules.evento.entity.StatusEvento;
import br.com.churchmanager.modules.evento.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping("/eventos")
@RequiredArgsConstructor
@Tag(name = "Eventos e Celebrações", description = "Endpoints para gerenciamento do calendário e eventos da igreja")
public class EventoController {

    private final EventoService eventoService;

    @GetMapping
    @Operation(summary = "Listar eventos paroquiais com filtros de período, comunidade e status")
    public ResponseEntity<List<EventoDTO.Response>> listar(
            @RequestParam(required = false) UUID comunidadeId,
            @RequestParam(required = false) StatusEvento status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fim,
            @RequestParam(required = false) String busca) {
        return ResponseEntity.ok(eventoService.listar(comunidadeId, status, inicio, fim, busca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID")
    public ResponseEntity<EventoDTO.Response> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo evento ou celebração")
    public ResponseEntity<EventoDTO.Response> criar(@Valid @RequestBody EventoDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.criar(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de um evento")
    public ResponseEntity<EventoDTO.Response> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody EventoDTO.Request req) {
        return ResponseEntity.ok(eventoService.atualizar(id, req));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status de um evento (ex: AGENDADO, EM_ANDAMENTO, CONCLUIDO, CANCELADO)")
    public ResponseEntity<EventoDTO.Response> atualizarStatus(
            @PathVariable UUID id,
            @RequestParam StatusEvento status) {
        return ResponseEntity.ok(eventoService.atualizarStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover evento")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
