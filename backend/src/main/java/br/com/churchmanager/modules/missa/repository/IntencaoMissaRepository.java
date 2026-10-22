package br.com.churchmanager.modules.missa.repository;

import br.com.churchmanager.modules.missa.entity.IntencaoMissa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface IntencaoMissaRepository extends JpaRepository<IntencaoMissa, UUID> {
    List<IntencaoMissa> findByHorarioMissaId(UUID horarioMissaId);
    List<IntencaoMissa> findByDataCelebracao(LocalDate dataCelebracao);
    List<IntencaoMissa> findByHorarioMissaIdAndDataCelebracao(UUID horarioMissaId, LocalDate dataCelebracao);
}
