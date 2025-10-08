package br.pucpr.checkinexpress.repository;

import br.pucpr.checkinexpress.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu endereço de e-mail.
     * O Spring Data JPA cria a implementação deste método automaticamente
     * baseado no nome do método. "findByEmail" é traduzido para a consulta SQL
     * "SELECT * FROM users WHERE email = ?".
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return um Optional contendo o usuário, se encontrado, ou vazio caso contrário.
     */
    Optional<User> findByEmail(String email);

}
