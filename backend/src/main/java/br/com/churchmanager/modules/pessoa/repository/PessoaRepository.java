package br.com.churchmanager.modules.pessoa.repository;

import br.com.churchmanager.modules.pessoa.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, UUID> {
    List<Pessoa> findByNomeContainingIgnoreCase(String nome);
}
