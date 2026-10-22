package br.com.churchmanager.modules.inventario.repository;

import br.com.churchmanager.modules.inventario.entity.MovimentacaoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentacaoItemRepository extends JpaRepository<MovimentacaoItem, UUID> {
    List<MovimentacaoItem> findByItemIdOrderByDataMovimentacaoDesc(UUID itemId);
}
