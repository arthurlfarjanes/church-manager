package br.com.churchmanager.modules.pessoa.controller;

import br.com.churchmanager.modules.pessoa.dto.PessoaDTO;
import br.com.churchmanager.modules.pessoa.service.PessoaService;
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
@RequestMapping("/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "Endpoints para paroquianos, voluntários e agentes pastorais")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    @Operation(summary = "Listar pessoas com busca opcional por nome")
    public ResponseEntity<List<PessoaDTO.Response>> listar(@RequestParam(required = false) String busca) {
        return ResponseEntity.ok(pessoaService.listar(busca));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID")
    public ResponseEntity<PessoaDTO.Response> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pessoaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar nova pessoa")
    public ResponseEntity<PessoaDTO.Response> criar(@Valid @RequestBody PessoaDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaService.criar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cadastro de pessoa")
    public ResponseEntity<PessoaDTO.Response> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody PessoaDTO.Request request) {
        return ResponseEntity.ok(pessoaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover pessoa")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        pessoaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
