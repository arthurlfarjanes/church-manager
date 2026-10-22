package br.com.churchmanager.modules.pastoral.repository;

import br.com.churchmanager.modules.pastoral.entity.PastoralMembro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PastoralMembroRepository extends JpaRepository<PastoralMembro, UUID> {
    List<PastoralMembro> findByPastoralId(UUID pastoralId);
    List<PastoralMembro> findByPastoralIdAndAtivoTrue(UUID pastoralId);
    List<PastoralMembro> findByPessoaId(UUID pessoaId);
    Optional<PastoralMembro> findByPastoralIdAndPessoaId(UUID pastoralId, UUID pessoaId);
    long countByPastoralIdAndAtivoTrue(UUID pastoralId);
}
