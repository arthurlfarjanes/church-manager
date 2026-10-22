package br.com.churchmanager.modules.paroquia.repository;

import br.com.churchmanager.modules.paroquia.entity.Paroquia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParoquiaRepository extends JpaRepository<Paroquia, UUID> {
}
