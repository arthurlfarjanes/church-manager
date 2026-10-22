package br.com.churchmanager.modules.pastoral.repository;

import br.com.churchmanager.modules.pastoral.entity.AtividadePastoral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AtividadePastoralRepository extends JpaRepository<AtividadePastoral, UUID> {
    List<AtividadePastoral> findByPastoralIdOrderByDataHoraDesc(UUID pastoralId);
    List<AtividadePastoral> findByPastoralIdAndDataHoraBetween(UUID pastoralId, OffsetDateTime inicio, OffsetDateTime fim);
}
