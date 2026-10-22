package br.com.churchmanager.modules.inventario.repository;

import br.com.churchmanager.modules.inventario.entity.ItemPatrimonio;
import br.com.churchmanager.modules.inventario.entity.StatusItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemPatrimonioRepository extends JpaRepository<ItemPatrimonio, UUID> {
    List<ItemPatrimonio> findByCategoriaId(UUID categoriaId);
    List<ItemPatrimonio> findByComunidadeId(UUID comunidadeId);
    List<ItemPatrimonio> findByStatus(StatusItem status);
    List<ItemPatrimonio> findByNomeContainingIgnoreCase(String nome);
}
