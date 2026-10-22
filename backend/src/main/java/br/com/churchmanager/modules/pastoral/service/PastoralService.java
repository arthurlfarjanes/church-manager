package br.com.churchmanager.modules.pastoral.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import br.com.churchmanager.modules.paroquia.repository.ComunidadeRepository;
import br.com.churchmanager.modules.pastoral.dto.AtividadePastoralDTO;
import br.com.churchmanager.modules.pastoral.dto.PastoralDTO;
import br.com.churchmanager.modules.pastoral.dto.PastoralMembroDTO;
import br.com.churchmanager.modules.pastoral.entity.AtividadePastoral;
import br.com.churchmanager.modules.pastoral.entity.Pastoral;
import br.com.churchmanager.modules.pastoral.entity.PastoralMembro;
import br.com.churchmanager.modules.pastoral.repository.AtividadePastoralRepository;
import br.com.churchmanager.modules.pastoral.repository.PastoralMembroRepository;
import br.com.churchmanager.modules.pastoral.repository.PastoralRepository;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import br.com.churchmanager.modules.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PastoralService {

    private final PastoralRepository pastoralRepository;
    private final PastoralMembroRepository membroRepository;
    private final AtividadePastoralRepository atividadeRepository;
    private final PessoaRepository pessoaRepository;
    private final ComunidadeRepository comunidadeRepository;

    // --- Pastoral CRUD ---
    @Transactional(readOnly = true)
    public List<PastoralDTO.Response> listar(UUID comunidadeId, Boolean apenasAtivos, String busca) {
        List<Pastoral> pastorais;
        if (busca != null && !busca.isBlank()) {
            pastorais = pastoralRepository.findByNomeContainingIgnoreCase(busca);
        } else if (comunidadeId != null) {
            pastorais = pastoralRepository.findByComunidadeId(comunidadeId);
        } else if (Boolean.TRUE.equals(apenasAtivos)) {
            pastorais = pastoralRepository.findByAtivoTrue();
        } else {
            pastorais = pastoralRepository.findAll();
        }
        return pastorais.stream().map(this::toPastoralResponse).toList();
    }

    @Transactional(readOnly = true)
    public PastoralDTO.Response buscarPorId(UUID id) {
        return toPastoralResponse(getPastoralOrThrow(id));
    }

    public PastoralDTO.Response criar(PastoralDTO.Request req) {
        Pessoa coordenador = req.coordenadorId() != null
                ? pessoaRepository.findById(req.coordenadorId()).orElse(null)
                : null;
        Pessoa vice = req.viceCoordenadorId() != null
                ? pessoaRepository.findById(req.viceCoordenadorId()).orElse(null)
                : null;
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;

        Pastoral pastoral = Pastoral.builder()
                .nome(req.nome())
                .descricao(req.descricao())
                .coordenador(coordenador)
                .viceCoordenador(vice)
                .comunidade(comunidade)
                .ativo(req.ativo() != null ? req.ativo() : true)
                .build();

        return toPastoralResponse(pastoralRepository.save(pastoral));
    }

    public PastoralDTO.Response atualizar(UUID id, PastoralDTO.Request req) {
        Pastoral pastoral = getPastoralOrThrow(id);
        Pessoa coordenador = req.coordenadorId() != null
                ? pessoaRepository.findById(req.coordenadorId()).orElse(null)
                : null;
        Pessoa vice = req.viceCoordenadorId() != null
                ? pessoaRepository.findById(req.viceCoordenadorId()).orElse(null)
                : null;
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;

        pastoral.setNome(req.nome());
        pastoral.setDescricao(req.descricao());
        pastoral.setCoordenador(coordenador);
        pastoral.setViceCoordenador(vice);
        pastoral.setComunidade(comunidade);
        if (req.ativo() != null) pastoral.setAtivo(req.ativo());

        return toPastoralResponse(pastoralRepository.save(pastoral));
    }

    public void deletar(UUID id) {
        Pastoral pastoral = getPastoralOrThrow(id);
        pastoralRepository.delete(pastoral);
    }

    // --- Membros ---
    @Transactional(readOnly = true)
    public List<PastoralMembroDTO.Response> listarMembros(UUID pastoralId, Boolean apenasAtivos) {
        getPastoralOrThrow(pastoralId);
        List<PastoralMembro> membros = Boolean.TRUE.equals(apenasAtivos)
                ? membroRepository.findByPastoralIdAndAtivoTrue(pastoralId)
                : membroRepository.findByPastoralId(pastoralId);

        return membros.stream().map(this::toMembroResponse).toList();
    }

    public PastoralMembroDTO.Response adicionarMembro(PastoralMembroDTO.Request req) {
        Pastoral pastoral = getPastoralOrThrow(req.pastoralId());
        Pessoa pessoa = pessoaRepository.findById(req.pessoaId())
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa", req.pessoaId()));

        PastoralMembro membro = membroRepository.findByPastoralIdAndPessoaId(req.pastoralId(), req.pessoaId())
                .orElse(null);

        if (membro != null) {
            // Se já existia, reativa e atualiza função
            membro.setAtivo(true);
            if (req.funcao() != null) membro.setFuncao(req.funcao());
            return toMembroResponse(membroRepository.save(membro));
        }

        membro = PastoralMembro.builder()
                .pastoral(pastoral)
                .pessoa(pessoa)
                .funcao(req.funcao() != null ? req.funcao() : "AGENTE")
                .dataIngresso(req.dataIngresso() != null ? req.dataIngresso() : LocalDate.now())
                .ativo(req.ativo() != null ? req.ativo() : true)
                .build();

        return toMembroResponse(membroRepository.save(membro));
    }

    public PastoralMembroDTO.Response atualizarMembro(UUID membroId, PastoralMembroDTO.UpdateRequest req) {
        PastoralMembro membro = membroRepository.findById(membroId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro da pastoral", membroId));

        if (req.funcao() != null) membro.setFuncao(req.funcao());
        if (req.ativo() != null) membro.setAtivo(req.ativo());

        return toMembroResponse(membroRepository.save(membro));
    }

    public void removerMembro(UUID membroId) {
        PastoralMembro membro = membroRepository.findById(membroId)
                .orElseThrow(() -> new ResourceNotFoundException("Membro da pastoral", membroId));
        membroRepository.delete(membro);
    }

    // --- Atividades ---
    @Transactional(readOnly = true)
    public List<AtividadePastoralDTO.Response> listarAtividades(UUID pastoralId, OffsetDateTime inicio, OffsetDateTime fim) {
        getPastoralOrThrow(pastoralId);
        List<AtividadePastoral> atividades;
        if (inicio != null && fim != null) {
            atividades = atividadeRepository.findByPastoralIdAndDataHoraBetween(pastoralId, inicio, fim);
        } else {
            atividades = atividadeRepository.findByPastoralIdOrderByDataHoraDesc(pastoralId);
        }
        return atividades.stream().map(this::toAtividadeResponse).toList();
    }

    public AtividadePastoralDTO.Response criarAtividade(AtividadePastoralDTO.Request req) {
        Pastoral pastoral = getPastoralOrThrow(req.pastoralId());

        AtividadePastoral atividade = AtividadePastoral.builder()
                .pastoral(pastoral)
                .titulo(req.titulo())
                .descricao(req.descricao())
                .dataHora(req.dataHora())
                .local(req.local())
                .status(req.status() != null ? req.status() : "AGENDADA")
                .build();

        return toAtividadeResponse(atividadeRepository.save(atividade));
    }

    public AtividadePastoralDTO.Response atualizarAtividade(UUID id, AtividadePastoralDTO.Request req) {
        AtividadePastoral atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atividade pastoral", id));
        Pastoral pastoral = getPastoralOrThrow(req.pastoralId());

        atividade.setPastoral(pastoral);
        atividade.setTitulo(req.titulo());
        atividade.setDescricao(req.descricao());
        atividade.setDataHora(req.dataHora());
        atividade.setLocal(req.local());
        if (req.status() != null) atividade.setStatus(req.status());

        return toAtividadeResponse(atividadeRepository.save(atividade));
    }

    public void deletarAtividade(UUID id) {
        AtividadePastoral atividade = atividadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Atividade pastoral", id));
        atividadeRepository.delete(atividade);
    }

    // --- Helpers / Mappers ---
    private Pastoral getPastoralOrThrow(UUID id) {
        return pastoralRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pastoral", id));
    }

    private PastoralDTO.Response toPastoralResponse(Pastoral p) {
        long totalMembros = membroRepository.countByPastoralIdAndAtivoTrue(p.getId());
        return new PastoralDTO.Response(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getCoordenador() != null ? p.getCoordenador().getId() : null,
                p.getCoordenador() != null ? p.getCoordenador().getNome() : null,
                p.getViceCoordenador() != null ? p.getViceCoordenador().getId() : null,
                p.getViceCoordenador() != null ? p.getViceCoordenador().getNome() : null,
                p.getComunidade() != null ? p.getComunidade().getId() : null,
                p.getComunidade() != null ? p.getComunidade().getNome() : null,
                p.getAtivo(),
                totalMembros,
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    private PastoralMembroDTO.Response toMembroResponse(PastoralMembro m) {
        return new PastoralMembroDTO.Response(
                m.getId(),
                m.getPastoral().getId(),
                m.getPastoral().getNome(),
                m.getPessoa().getId(),
                m.getPessoa().getNome(),
                m.getPessoa().getTelefone(),
                m.getPessoa().getEmail(),
                m.getFuncao(),
                m.getDataIngresso(),
                m.getAtivo(),
                m.getCreatedAt(),
                m.getUpdatedAt()
        );
    }

    private AtividadePastoralDTO.Response toAtividadeResponse(AtividadePastoral a) {
        return new AtividadePastoralDTO.Response(
                a.getId(),
                a.getPastoral().getId(),
                a.getPastoral().getNome(),
                a.getTitulo(),
                a.getDescricao(),
                a.getDataHora(),
                a.getLocal(),
                a.getStatus(),
                a.getCreatedAt(),
                a.getUpdatedAt()
        );
    }
}
