package br.pucpr.checkinexpress.repository;

import br.pucpr.checkinexpress.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.pucpr.checkinexpress.security.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método obrigatório para o login: buscar o usuário pelo email
    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);
}