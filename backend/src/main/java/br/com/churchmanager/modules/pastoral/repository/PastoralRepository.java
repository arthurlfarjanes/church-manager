package br.com.churchmanager.modules.pastoral.repository;

import br.com.churchmanager.modules.pastoral.entity.Pastoral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PastoralRepository extends JpaRepository<Pastoral, UUID> {
    List<Pastoral> findByAtivoTrue();
    List<Pastoral> findByComunidadeId(UUID comunidadeId);
    List<Pastoral> findByNomeContainingIgnoreCase(String nome);
}
