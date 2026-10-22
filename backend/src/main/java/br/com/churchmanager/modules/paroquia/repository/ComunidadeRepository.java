package br.com.churchmanager.modules.paroquia.repository;

import br.com.churchmanager.modules.paroquia.entity.Comunidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComunidadeRepository extends JpaRepository<Comunidade, UUID> {
    List<Comunidade> findByParoquiaId(UUID paroquiaId);
}
