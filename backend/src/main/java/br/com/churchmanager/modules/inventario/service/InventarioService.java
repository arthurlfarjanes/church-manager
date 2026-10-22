package br.com.churchmanager.modules.inventario.service;

import br.com.churchmanager.common.exception.ResourceNotFoundException;
import br.com.churchmanager.modules.inventario.dto.CategoriaItemDTO;
import br.com.churchmanager.modules.inventario.dto.ItemPatrimonioDTO;
import br.com.churchmanager.modules.inventario.dto.MovimentacaoItemDTO;
import br.com.churchmanager.modules.inventario.entity.CategoriaItem;
import br.com.churchmanager.modules.inventario.entity.EstadoConservacao;
import br.com.churchmanager.modules.inventario.entity.ItemPatrimonio;
import br.com.churchmanager.modules.inventario.entity.MovimentacaoItem;
import br.com.churchmanager.modules.inventario.entity.StatusItem;
import br.com.churchmanager.modules.inventario.entity.TipoMovimentacao;
import br.com.churchmanager.modules.inventario.repository.CategoriaItemRepository;
import br.com.churchmanager.modules.inventario.repository.ItemPatrimonioRepository;
import br.com.churchmanager.modules.inventario.repository.MovimentacaoItemRepository;
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
public class InventarioService {

    private final CategoriaItemRepository categoriaRepository;
    private final ItemPatrimonioRepository itemRepository;
    private final MovimentacaoItemRepository movimentacaoRepository;
    private final ComunidadeRepository comunidadeRepository;
    private final PessoaRepository pessoaRepository;

    // --- Categorias ---
    @Transactional(readOnly = true)
    public List<CategoriaItemDTO.Response> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::toCategoriaResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaItemDTO.Response buscarCategoriaPorId(UUID id) {
        return toCategoriaResponse(getCategoriaOrThrow(id));
    }

    public CategoriaItemDTO.Response criarCategoria(CategoriaItemDTO.Request req) {
        CategoriaItem categoria = CategoriaItem.builder()
                .nome(req.nome())
                .descricao(req.descricao())
                .build();
        return toCategoriaResponse(categoriaRepository.save(categoria));
    }

    public CategoriaItemDTO.Response atualizarCategoria(UUID id, CategoriaItemDTO.Request req) {
        CategoriaItem categoria = getCategoriaOrThrow(id);
        categoria.setNome(req.nome());
        categoria.setDescricao(req.descricao());
        return toCategoriaResponse(categoriaRepository.save(categoria));
    }

    public void deletarCategoria(UUID id) {
        CategoriaItem categoria = getCategoriaOrThrow(id);
        categoriaRepository.delete(categoria);
    }

    // --- Itens de Patrimônio ---
    @Transactional(readOnly = true)
    public List<ItemPatrimonioDTO.Response> listarItens(UUID categoriaId, UUID comunidadeId, StatusItem status, String busca) {
        List<ItemPatrimonio> itens;
        if (busca != null && !busca.isBlank()) {
            itens = itemRepository.findByNomeContainingIgnoreCase(busca);
        } else if (categoriaId != null) {
            itens = itemRepository.findByCategoriaId(categoriaId);
        } else if (comunidadeId != null) {
            itens = itemRepository.findByComunidadeId(comunidadeId);
        } else if (status != null) {
            itens = itemRepository.findByStatus(status);
        } else {
            itens = itemRepository.findAll();
        }
        return itens.stream().map(this::toItemResponse).toList();
    }

    @Transactional(readOnly = true)
    public ItemPatrimonioDTO.Response buscarItemPorId(UUID id) {
        return toItemResponse(getItemOrThrow(id));
    }

    public ItemPatrimonioDTO.Response criarItem(ItemPatrimonioDTO.Request req) {
        CategoriaItem categoria = getCategoriaOrThrow(req.categoriaId());
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;

        ItemPatrimonio item = ItemPatrimonio.builder()
                .codigoTombamento(req.codigoTombamento())
                .nome(req.nome())
                .descricao(req.descricao())
                .categoria(categoria)
                .comunidade(comunidade)
                .localizacao(req.localizacao())
                .quantidade(req.quantidade() != null ? req.quantidade() : 1)
                .valorEstimado(req.valorEstimado())
                .estadoConservacao(req.estadoConservacao() != null ? req.estadoConservacao() : EstadoConservacao.BOM)
                .status(req.status() != null ? req.status() : StatusItem.DISPONIVEL)
                .dataAquisicao(req.dataAquisicao())
                .build();

        return toItemResponse(itemRepository.save(item));
    }

    public ItemPatrimonioDTO.Response atualizarItem(UUID id, ItemPatrimonioDTO.Request req) {
        ItemPatrimonio item = getItemOrThrow(id);
        CategoriaItem categoria = getCategoriaOrThrow(req.categoriaId());
        Comunidade comunidade = req.comunidadeId() != null
                ? comunidadeRepository.findById(req.comunidadeId()).orElse(null)
                : null;

        item.setCodigoTombamento(req.codigoTombamento());
        item.setNome(req.nome());
        item.setDescricao(req.descricao());
        item.setCategoria(categoria);
        item.setComunidade(comunidade);
        item.setLocalizacao(req.localizacao());
        if (req.quantidade() != null) item.setQuantidade(req.quantidade());
        item.setValorEstimado(req.valorEstimado());
        if (req.estadoConservacao() != null) item.setEstadoConservacao(req.estadoConservacao());
        if (req.status() != null) item.setStatus(req.status());
        item.setDataAquisicao(req.dataAquisicao());

        return toItemResponse(itemRepository.save(item));
    }

    public void deletarItem(UUID id) {
        ItemPatrimonio item = getItemOrThrow(id);
        itemRepository.delete(item);
    }

    // --- Movimentações ---
    @Transactional(readOnly = true)
    public List<MovimentacaoItemDTO.Response> listarMovimentacoesPorItem(UUID itemId) {
        getItemOrThrow(itemId);
        return movimentacaoRepository.findByItemIdOrderByDataMovimentacaoDesc(itemId).stream()
                .map(this::toMovimentacaoResponse)
                .toList();
    }

    public MovimentacaoItemDTO.Response registrarMovimentacao(MovimentacaoItemDTO.Request req) {
        ItemPatrimonio item = getItemOrThrow(req.itemId());
        Pessoa responsavel = req.responsavelId() != null
                ? pessoaRepository.findById(req.responsavelId()).orElse(null)
                : null;

        MovimentacaoItem mov = MovimentacaoItem.builder()
                .item(item)
                .tipo(req.tipo())
                .origem(req.origem())
                .destino(req.destino())
                .responsavel(responsavel)
                .observacoes(req.observacoes())
                .dataMovimentacao(OffsetDateTime.now())
                .build();

        // Atualiza status do item conforme o tipo de movimentação
        if (req.tipo() == TipoMovimentacao.EMPRESTIMO) {
            item.setStatus(StatusItem.EMPRESTADO);
        } else if (req.tipo() == TipoMovimentacao.DEVOLUCAO) {
            item.setStatus(StatusItem.DISPONIVEL);
            if (req.destino() != null) {
                item.setLocalizacao(req.destino());
            }
        } else if (req.tipo() == TipoMovimentacao.MANUTENCAO) {
            item.setStatus(StatusItem.EM_MANUTENCAO);
        } else if (req.tipo() == TipoMovimentacao.BAIXA) {
            item.setStatus(StatusItem.BAIXADO);
        } else if (req.tipo() == TipoMovimentacao.TRANSFERENCIA && req.destino() != null) {
            item.setLocalizacao(req.destino());
        }
        itemRepository.save(item);

        return toMovimentacaoResponse(movimentacaoRepository.save(mov));
    }

    // --- Helpers / Mappers ---
    private CategoriaItem getCategoriaOrThrow(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de item", id));
    }

    private ItemPatrimonio getItemOrThrow(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de patrimônio", id));
    }

    private CategoriaItemDTO.Response toCategoriaResponse(CategoriaItem c) {
        return new CategoriaItemDTO.Response(
                c.getId(),
                c.getNome(),
                c.getDescricao(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }

    private ItemPatrimonioDTO.Response toItemResponse(ItemPatrimonio item) {
        return new ItemPatrimonioDTO.Response(
                item.getId(),
                item.getCodigoTombamento(),
                item.getNome(),
                item.getDescricao(),
                item.getCategoria().getId(),
                item.getCategoria().getNome(),
                item.getComunidade() != null ? item.getComunidade().getId() : null,
                item.getComunidade() != null ? item.getComunidade().getNome() : null,
                item.getLocalizacao(),
                item.getQuantidade(),
                item.getValorEstimado(),
                item.getEstadoConservacao(),
                item.getStatus(),
                item.getDataAquisicao(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    private MovimentacaoItemDTO.Response toMovimentacaoResponse(MovimentacaoItem m) {
        return new MovimentacaoItemDTO.Response(
                m.getId(),
                m.getItem().getId(),
                m.getItem().getNome(),
                m.getTipo(),
                m.getDataMovimentacao(),
                m.getOrigem(),
                m.getDestino(),
                m.getResponsavel() != null ? m.getResponsavel().getId() : null,
                m.getResponsavel() != null ? m.getResponsavel().getNome() : null,
                m.getObservacoes(),
                m.getCreatedAt()
        );
    }
}
