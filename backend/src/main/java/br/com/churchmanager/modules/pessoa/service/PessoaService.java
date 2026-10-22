package br.com.churchmanager.modules.pessoa.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.pessoa.dto.PessoaDTO;
import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import br.com.churchmanager.modules.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    @Transactional(readOnly = true)
    public List<PessoaDTO.Response> listar(String busca) {
        List<Pessoa> lista = (busca != null && !busca.isBlank())
                ? pessoaRepository.findByNomeContainingIgnoreCase(busca)
                : pessoaRepository.findAll();

        return lista.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PessoaDTO.Response buscarPorId(UUID id) {
        return toResponse(getPessoaOrThrow(id));
    }

    public PessoaDTO.Response criar(PessoaDTO.Request req) {
        Pessoa pessoa = Pessoa.builder()
                .nome(req.nome())
                .email(req.email())
                .telefone(req.telefone())
                .dataNascimento(req.dataNascimento())
                .endereco(req.endereco())
                .observacoes(req.observacoes())
                .build();
        return toResponse(pessoaRepository.save(pessoa));
    }

    public PessoaDTO.Response atualizar(UUID id, PessoaDTO.Request req) {
        Pessoa pessoa = getPessoaOrThrow(id);
        pessoa.setNome(req.nome());
        pessoa.setEmail(req.email());
        pessoa.setTelefone(req.telefone());
        pessoa.setDataNascimento(req.dataNascimento());
        pessoa.setEndereco(req.endereco());
        pessoa.setObservacoes(req.observacoes());
        return toResponse(pessoaRepository.save(pessoa));
    }

    public void deletar(UUID id) {
        Pessoa pessoa = getPessoaOrThrow(id);
        pessoaRepository.delete(pessoa);
    }

    private Pessoa getPessoaOrThrow(UUID id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa", id));
    }

    public PessoaDTO.Response toResponse(Pessoa p) {
        return new PessoaDTO.Response(
                p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getTelefone(),
                p.getDataNascimento(),
                p.getEndereco(),
                p.getObservacoes(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
