package br.pucpr.checkinexpress.repository;

import br.pucpr.checkinexpress.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    // Método para evitar serviços com o mesmo nome
    Optional<Servico> findByNome(String nome);
}