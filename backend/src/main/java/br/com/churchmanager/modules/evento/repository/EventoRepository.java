package br.com.churchmanager.modules.evento.repository;

import br.com.churchmanager.modules.evento.entity.Evento;
import br.com.churchmanager.modules.evento.entity.StatusEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventoRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByComunidadeId(UUID comunidadeId);
    List<Evento> findByStatus(StatusEvento status);
    List<Evento> findByDataInicioBetweenOrderByDataInicioAsc(OffsetDateTime start, OffsetDateTime end);
    List<Evento> findByTituloContainingIgnoreCase(String titulo);
}
