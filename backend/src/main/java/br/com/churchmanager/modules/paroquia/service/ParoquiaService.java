package br.com.churchmanager.modules.paroquia.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.paroquia.dto.ComunidadeDTO;
import br.com.churchmanager.modules.paroquia.dto.ParoquiaDTO;
import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import br.com.churchmanager.modules.paroquia.entity.Paroquia;
import br.com.churchmanager.modules.paroquia.repository.ComunidadeRepository;
import br.com.churchmanager.modules.paroquia.repository.ParoquiaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ParoquiaService {

    private final ParoquiaRepository paroquiaRepository;
    private final ComunidadeRepository comunidadeRepository;

    @Transactional(readOnly = true)
    public List<ParoquiaDTO.Response> listarTodas() {
        return paroquiaRepository.findAll().stream()
                .map(this::toParoquiaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ParoquiaDTO.Response buscarPorId(UUID id) {
        return toParoquiaResponse(getParoquiaOrThrow(id));
    }

    public ParoquiaDTO.Response criar(ParoquiaDTO.Request req) {
        Paroquia paroquia = Paroquia.builder()
                .nome(req.nome())
                .diocese(req.diocese())
                .paroco(req.paroco())
                .telefone(req.telefone())
                .email(req.email())
                .endereco(req.endereco())
                .cidade(req.cidade())
                .estado(req.estado())
                .build();
        return toParoquiaResponse(paroquiaRepository.save(paroquia));
    }

    public ParoquiaDTO.Response atualizar(UUID id, ParoquiaDTO.Request req) {
        Paroquia paroquia = getParoquiaOrThrow(id);
        paroquia.setNome(req.nome());
        paroquia.setDiocese(req.diocese());
        paroquia.setParoco(req.paroco());
        paroquia.setTelefone(req.telefone());
        paroquia.setEmail(req.email());
        paroquia.setEndereco(req.endereco());
        paroquia.setCidade(req.cidade());
        paroquia.setEstado(req.estado());
        return toParoquiaResponse(paroquiaRepository.save(paroquia));
    }

    public void deletar(UUID id) {
        Paroquia paroquia = getParoquiaOrThrow(id);
        paroquiaRepository.delete(paroquia);
    }

    // Comunidades
    @Transactional(readOnly = true)
    public List<ComunidadeDTO.Response> listarComunidades(UUID paroquiaId) {
        return comunidadeRepository.findByParoquiaId(paroquiaId).stream()
                .map(this::toComunidadeResponse)
                .toList();
    }

    public ComunidadeDTO.Response criarComunidade(ComunidadeDTO.Request req) {
        Paroquia paroquia = getParoquiaOrThrow(req.paroquiaId());
        Comunidade comunidade = Comunidade.builder()
                .paroquia(paroquia)
                .nome(req.nome())
                .endereco(req.endereco())
                .isMatriz(req.isMatriz() != null ? req.isMatriz() : false)
                .build();
        return toComunidadeResponse(comunidadeRepository.save(comunidade));
    }

    public void deletarComunidade(UUID id) {
        Comunidade comunidade = comunidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade", id));
        comunidadeRepository.delete(comunidade);
    }

    private Paroquia getParoquiaOrThrow(UUID id) {
        return paroquiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paróquia", id));
    }

    private ParoquiaDTO.Response toParoquiaResponse(Paroquia p) {
        return new ParoquiaDTO.Response(
                p.getId(),
                p.getNome(),
                p.getDiocese(),
                p.getParoco(),
                p.getTelefone(),
                p.getEmail(),
                p.getEndereco(),
                p.getCidade(),
                p.getEstado(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    private ComunidadeDTO.Response toComunidadeResponse(Comunidade c) {
        return new ComunidadeDTO.Response(
                c.getId(),
                c.getParoquia().getId(),
                c.getParoquia().getNome(),
                c.getNome(),
                c.getEndereco(),
                c.getIsMatriz(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}
