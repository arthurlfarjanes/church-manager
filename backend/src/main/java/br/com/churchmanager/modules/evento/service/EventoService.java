package br.com.churchmanager.modules.evento.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.evento.dto.EventoDTO;
import br.com.churchmanager.modules.evento.entity.Evento;
import br.com.churchmanager.modules.evento.entity.StatusEvento;
import br.com.churchmanager.modules.evento.repository.EventoRepository;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import br.com.churchmanager.modules.paroquia.repository.ComunidadeRepository;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import br.com.churchmanager.modules.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EventoService {

    private final EventoRepository eventoRepository;
    private final ComunidadeRepository comunidadeRepository;
    private final PessoaRepository pessoaRepository;

    @Transactional(readOnly = true)
    public List<EventoDTO.Response> listar(UUID comunidadeId, StatusEvento status, OffsetDateTime inicio, OffsetDateTime fim, String busca) {
        List<Evento> eventos;
        if (busca != null && !busca.isBlank()) {
            eventos = eventoRepository.findByTituloContainingIgnoreCase(busca);
        } else if (inicio != null && fim != null) {
            eventos = eventoRepository.findByDataInicioBetweenOrderByDataInicioAsc(inicio, fim);
        } else if (comunidadeId != null) {
            eventos = eventoRepository.findByComunidadeId(comunidadeId);
        } else if (status != null) {
            eventos = eventoRepository.findByStatus(status);
        } else {
            eventos = eventoRepository.findAll();
        }
        return eventos.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EventoDTO.Response buscarPorId(UUID id) {
        return toResponse(getEventoOrThrow(id));
    }

    public EventoDTO.Response criar(EventoDTO.Request req) {
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;
        Pessoa responsavel = req.responsavelId() != null
                ? pessoaRepository.findById(req.responsavelId()).orElse(null)
                : null;

        Evento evento = Evento.builder()
                .titulo(req.titulo())
                .descricao(req.descricao())
                .comunidade(comunidade)
                .localEspaco(req.localEspaco())
                .dataInicio(req.dataInicio())
                .dataFim(req.dataFim())
                .tipo(req.tipo() != null ? req.tipo() : "CELEBRACAO")
                .status(req.status() != null ? req.status() : StatusEvento.AGENDADO)
                .responsavel(responsavel)
                .build();

        return toResponse(eventoRepository.save(evento));
    }

    public EventoDTO.Response atualizar(UUID id, EventoDTO.Request req) {
        Evento evento = getEventoOrThrow(id);
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;
        Pessoa responsavel = req.responsavelId() != null
                ? pessoaRepository.findById(req.responsavelId()).orElse(null)
                : null;

        evento.setTitulo(req.titulo());
        evento.setDescricao(req.descricao());
        evento.setComunidade(comunidade);
        evento.setLocalEspaco(req.localEspaco());
        evento.setDataInicio(req.dataInicio());
        evento.setDataFim(req.dataFim());
        if (req.tipo() != null) evento.setTipo(req.tipo());
        if (req.status() != null) evento.setStatus(req.status());
        evento.setResponsavel(responsavel);

        return toResponse(eventoRepository.save(evento));
    }

    public EventoDTO.Response atualizarStatus(UUID id, StatusEvento status) {
        Evento evento = getEventoOrThrow(id);
        evento.setStatus(status);
        return toResponse(eventoRepository.save(evento));
    }

    public void deletar(UUID id) {
        Evento evento = getEventoOrThrow(id);
        eventoRepository.delete(evento);
    }

    private Evento getEventoOrThrow(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento", id));
    }

    private EventoDTO.Response toResponse(Evento e) {
        return new EventoDTO.Response(
                e.getId(),
                e.getTitulo(),
                e.getDescricao(),
                e.getComunidade() != null ? e.getComunidade().getId() : null,
                e.getComunidade() != null ? e.getComunidade().getNome() : null,
                e.getLocalEspaco(),
                e.getDataInicio(),
                e.getDataFim(),
                e.getTipo(),
                e.getStatus(),
                e.getResponsavel() != null ? e.getResponsavel().getId() : null,
                e.getResponsavel() != null ? e.getResponsavel().getNome() : null,
                e.getCreatedAt(),
                e.getUpdatedAt()
        );
    }
}
