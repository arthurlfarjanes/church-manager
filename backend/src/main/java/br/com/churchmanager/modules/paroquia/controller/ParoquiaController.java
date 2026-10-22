package br.com.churchmanager.modules.paroquia.controller;

import br.com.churchmanager.modules.paroquia.dto.ComunidadeDTO;
import br.com.churchmanager.modules.paroquia.dto.ParoquiaDTO;
import br.com.churchmanager.modules.paroquia.service.ParoquiaService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/paroquias")
@RequiredArgsConstructor
@Tag(name = "Paróquias e Comunidades", description = "Endpoints para gerenciamento da paróquia e suas capelas")
public class ParoquiaController {

    private final ParoquiaService paroquiaService;

    @GetMapping
    @Operation(summary = "Listar todas as paróquias cadastradas")
    public ResponseEntity<List<ParoquiaDTO.Response>> listar() {
        return ResponseEntity.ok(paroquiaService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter dados de uma paróquia por ID")
    public ResponseEntity<ParoquiaDTO.Response> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(paroquiaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova paróquia")
    public ResponseEntity<ParoquiaDTO.Response> criar(@Valid @RequestBody ParoquiaDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paroquiaService.criar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de uma paróquia")
    public ResponseEntity<ParoquiaDTO.Response> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ParoquiaDTO.Request request) {
        return ResponseEntity.ok(paroquiaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma paróquia")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        paroquiaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Comunidades / Capelas
    @GetMapping("/{id}/comunidades")
    @Operation(summary = "Listar comunidades/capelas da paróquia")
    public ResponseEntity<List<ComunidadeDTO.Response>> listarComunidades(@PathVariable UUID id) {
        return ResponseEntity.ok(paroquiaService.listarComunidades(id));
    }

    @PostMapping("/comunidades")
    @Operation(summary = "Cadastrar uma nova comunidade/capela")
    public ResponseEntity<ComunidadeDTO.Response> criarComunidade(@Valid @RequestBody ComunidadeDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paroquiaService.criarComunidade(request));
    }

    @DeleteMapping("/comunidades/{id}")
    @Operation(summary = "Remover uma comunidade/capela")
    public ResponseEntity<Void> deletarComunidade(@PathVariable UUID id) {
        paroquiaService.deletarComunidade(id);
        return ResponseEntity.noContent().build();
    }
}
