package br.com.churchmanager.modules.missa.controller;

import br.com.churchmanager.modules.missa.dto.HorarioMissaDTO;
import br.com.churchmanager.modules.missa.dto.IntencaoMissaDTO;
import br.com.churchmanager.modules.missa.entity.DiaSemana;
import br.com.churchmanager.modules.missa.service.MissaService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/missas")
@RequiredArgsConstructor
@Tag(name = "Missas e Celebrações", description = "Endpoints para gerenciamento de horários de missas e intenções")
public class MissaController {

    private final MissaService missaService;

    // --- Horários de Missa ---
    @GetMapping("/horarios")
    @Operation(summary = "Listar horários de missas com filtros opcionais")
    public ResponseEntity<List<HorarioMissaDTO.Response>> listarHorarios(
            @RequestParam(required = false) UUID comunidadeId,
            @RequestParam(required = false) DiaSemana diaSemana,
            @RequestParam(required = false, defaultValue = "false") Boolean apenasAtivos) {
        return ResponseEntity.ok(missaService.listarHorarios(comunidadeId, diaSemana, apenasAtivos));
    }

    @GetMapping("/horarios/{id}")
    @Operation(summary = "Buscar horário de missa por ID")
    public ResponseEntity<HorarioMissaDTO.Response> buscarHorarioPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(missaService.buscarHorarioPorId(id));
    }

    @PostMapping("/horarios")
    @Operation(summary = "Cadastrar novo horário de missa")
    public ResponseEntity<HorarioMissaDTO.Response> criarHorario(@Valid @RequestBody HorarioMissaDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(missaService.criarHorario(req));
    }

    @PutMapping("/horarios/{id}")
    @Operation(summary = "Atualizar horário de missa")
    public ResponseEntity<HorarioMissaDTO.Response> atualizarHorario(
            @PathVariable UUID id,
            @Valid @RequestBody HorarioMissaDTO.Request req) {
        return ResponseEntity.ok(missaService.atualizarHorario(id, req));
    }

    @DeleteMapping("/horarios/{id}")
    @Operation(summary = "Remover horário de missa")
    public ResponseEntity<Void> deletarHorario(@PathVariable UUID id) {
        missaService.deletarHorario(id);
        return ResponseEntity.noContent().build();
    }

    // --- Intenções de Missa ---
    @GetMapping("/intencoes")
    @Operation(summary = "Listar intenções de missa por horário e/ou data")
    public ResponseEntity<List<IntencaoMissaDTO.Response>> listarIntencoes(
            @RequestParam(required = false) UUID horarioMissaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(missaService.listarIntencoes(horarioMissaId, data));
    }

    @GetMapping("/intencoes/{id}")
    @Operation(summary = "Buscar intenção de missa por ID")
    public ResponseEntity<IntencaoMissaDTO.Response> buscarIntencaoPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(missaService.buscarIntencaoPorId(id));
    }

    @PostMapping("/intencoes")
    @Operation(summary = "Cadastrar nova intenção para a missa")
    public ResponseEntity<IntencaoMissaDTO.Response> criarIntencao(@Valid @RequestBody IntencaoMissaDTO.Request req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(missaService.criarIntencao(req));
    }

    @PutMapping("/intencoes/{id}")
    @Operation(summary = "Atualizar intenção de missa")
    public ResponseEntity<IntencaoMissaDTO.Response> atualizarIntencao(
            @PathVariable UUID id,
            @Valid @RequestBody IntencaoMissaDTO.Request req) {
        return ResponseEntity.ok(missaService.atualizarIntencao(id, req));
    }

    @DeleteMapping("/intencoes/{id}")
    @Operation(summary = "Remover intenção de missa")
    public ResponseEntity<Void> deletarIntencao(@PathVariable UUID id) {
        missaService.deletarIntencao(id);
        return ResponseEntity.noContent().build();
    }
}
