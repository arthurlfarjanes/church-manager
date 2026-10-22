package br.com.churchmanager.modules.missa.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.missa.dto.HorarioMissaDTO;
import br.com.churchmanager.modules.missa.dto.IntencaoMissaDTO;
import br.com.churchmanager.modules.missa.entity.DiaSemana;
import br.com.churchmanager.modules.missa.entity.HorarioMissa;
import br.com.churchmanager.modules.missa.entity.IntencaoMissa;
import br.com.churchmanager.modules.missa.repository.HorarioMissaRepository;
import br.com.churchmanager.modules.missa.repository.IntencaoMissaRepository;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import br.com.churchmanager.modules.paroquia.repository.ComunidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MissaService {

    private final HorarioMissaRepository horarioRepository;
    private final IntencaoMissaRepository intencaoRepository;
    private final ComunidadeRepository comunidadeRepository;

    // --- Horários de Missa ---
    @Transactional(readOnly = true)
    public List<HorarioMissaDTO.Response> listarHorarios(UUID comunidadeId, DiaSemana diaSemana, Boolean apenasAtivos) {
        List<HorarioMissa> horarios;
        if (comunidadeId != null && Boolean.TRUE.equals(apenasAtivos)) {
            horarios = horarioRepository.findByComunidadeIdAndAtivoTrue(comunidadeId);
        } else if (comunidadeId != null) {
            horarios = horarioRepository.findByComunidadeId(comunidadeId);
        } else if (diaSemana != null) {
            horarios = horarioRepository.findByDiaSemana(diaSemana);
        } else if (Boolean.TRUE.equals(apenasAtivos)) {
            horarios = horarioRepository.findByAtivoTrue();
        } else {
            horarios = horarioRepository.findAll();
        }
        return horarios.stream().map(this::toHorarioResponse).toList();
    }

    @Transactional(readOnly = true)
    public HorarioMissaDTO.Response buscarHorarioPorId(UUID id) {
        return toHorarioResponse(getHorarioOrThrow(id));
    }

    public HorarioMissaDTO.Response criarHorario(HorarioMissaDTO.Request req) {
        Comunidade comunidade = comunidadeRepository.findById(req.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade", req.comunidadeId()));

        HorarioMissa horario = HorarioMissa.builder()
                .comunidade(comunidade)
                .diaSemana(req.diaSemana())
                .horario(req.horario())
                .celebrante(req.celebrante())
                .descricao(req.descricao())
                .ativo(req.ativo() != null ? req.ativo() : true)
                .build();

        return toHorarioResponse(horarioRepository.save(horario));
    }

    public HorarioMissaDTO.Response atualizarHorario(UUID id, HorarioMissaDTO.Request req) {
        HorarioMissa horario = getHorarioOrThrow(id);
        Comunidade comunidade = comunidadeRepository.findById(req.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade", req.comunidadeId()));

        horario.setComunidade(comunidade);
        horario.setDiaSemana(req.diaSemana());
        horario.setHorario(req.horario());
        horario.setCelebrante(req.celebrante());
        horario.setDescricao(req.descricao());
        if (req.ativo() != null) horario.setAtivo(req.ativo());

        return toHorarioResponse(horarioRepository.save(horario));
    }

    public void deletarHorario(UUID id) {
        HorarioMissa horario = getHorarioOrThrow(id);
        horarioRepository.delete(horario);
    }

    // --- Intenções de Missa ---
    @Transactional(readOnly = true)
    public List<IntencaoMissaDTO.Response> listarIntencoes(UUID horarioMissaId, LocalDate data) {
        List<IntencaoMissa> intencoes;
        if (horarioMissaId != null && data != null) {
            intencoes = intencaoRepository.findByHorarioMissaIdAndDataCelebracao(horarioMissaId, data);
        } else if (horarioMissaId != null) {
            intencoes = intencaoRepository.findByHorarioMissaId(horarioMissaId);
        } else if (data != null) {
            intencoes = intencaoRepository.findByDataCelebracao(data);
        } else {
            intencoes = intencaoRepository.findAll();
        }
        return intencoes.stream().map(this::toIntencaoResponse).toList();
    }

    @Transactional(readOnly = true)
    public IntencaoMissaDTO.Response buscarIntencaoPorId(UUID id) {
        return toIntencaoResponse(getIntencaoOrThrow(id));
    }

    public IntencaoMissaDTO.Response criarIntencao(IntencaoMissaDTO.Request req) {
        HorarioMissa horario = getHorarioOrThrow(req.horarioMissaId());

        IntencaoMissa intencao = IntencaoMissa.builder()
                .horarioMissa(horario)
                .dataCelebracao(req.dataCelebracao())
                .tipo(req.tipo() != null ? req.tipo() : "FALECIMENTO")
                .paraQuem(req.paraQuem())
                .solicitante(req.solicitante())
                .valorEstipendio(req.valorEstipendio())
                .build();

        return toIntencaoResponse(intencaoRepository.save(intencao));
    }

    public IntencaoMissaDTO.Response atualizarIntencao(UUID id, IntencaoMissaDTO.Request req) {
        IntencaoMissa intencao = getIntencaoOrThrow(id);
        HorarioMissa horario = getHorarioOrThrow(req.horarioMissaId());

        intencao.setHorarioMissa(horario);
        intencao.setDataCelebracao(req.dataCelebracao());
        if (req.tipo() != null) intencao.setTipo(req.tipo());
        intencao.setParaQuem(req.paraQuem());
        intencao.setSolicitante(req.solicitante());
        intencao.setValorEstipendio(req.valorEstipendio());

        return toIntencaoResponse(intencaoRepository.save(intencao));
    }

    public void deletarIntencao(UUID id) {
        IntencaoMissa intencao = getIntencaoOrThrow(id);
        intencaoRepository.delete(intencao);
    }

    // --- Helpers / Mappers ---
    private HorarioMissa getHorarioOrThrow(UUID id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horário de missa", id));
    }

    private IntencaoMissa getIntencaoOrThrow(UUID id) {
        return intencaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intenção de missa", id));
    }

    private HorarioMissaDTO.Response toHorarioResponse(HorarioMissa h) {
        return new HorarioMissaDTO.Response(
                h.getId(),
                h.getComunidade().getId(),
                h.getComunidade().getNome(),
                h.getDiaSemana(),
                h.getHorario(),
                h.getCelebrante(),
                h.getDescricao(),
                h.getAtivo(),
                h.getCreatedAt(),
                h.getUpdatedAt()
        );
    }

    private IntencaoMissaDTO.Response toIntencaoResponse(IntencaoMissa i) {
        return new IntencaoMissaDTO.Response(
                i.getId(),
                i.getHorarioMissa().getId(),
                i.getHorarioMissa().getComunidade().getNome(),
                i.getHorarioMissa().getHorario(),
                i.getDataCelebracao(),
                i.getTipo(),
                i.getParaQuem(),
                i.getSolicitante(),
                i.getValorEstipendio(),
                i.getCreatedAt(),
                i.getUpdatedAt()
        );
    }
}
