package br.pucpr.checkinexpress.repository;

import br.pucpr.checkinexpress.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // Podemos buscar o registro do funcionário pelo ID do User
    Optional<Funcionario> findByUsuarioId(Long usuarioId);
}