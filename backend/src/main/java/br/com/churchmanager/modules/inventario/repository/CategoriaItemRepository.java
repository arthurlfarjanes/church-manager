package br.com.churchmanager.modules.inventario.repository;

import br.com.churchmanager.modules.inventario.entity.CategoriaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaItemRepository extends JpaRepository<CategoriaItem, UUID> {
    Optional<CategoriaItem> findByNomeIgnoreCase(String nome);
}
