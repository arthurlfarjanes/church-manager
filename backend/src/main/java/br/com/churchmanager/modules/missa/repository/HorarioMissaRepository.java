package br.com.churchmanager.modules.missa.repository;

import br.com.churchmanager.modules.missa.entity.DiaSemana;
import br.com.churchmanager.modules.missa.entity.HorarioMissa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HorarioMissaRepository extends JpaRepository<HorarioMissa, UUID> {
    List<HorarioMissa> findByComunidadeId(UUID comunidadeId);
    List<HorarioMissa> findByDiaSemana(DiaSemana diaSemana);
    List<HorarioMissa> findByAtivoTrue();
    List<HorarioMissa> findByComunidadeIdAndAtivoTrue(UUID comunidadeId);
}
