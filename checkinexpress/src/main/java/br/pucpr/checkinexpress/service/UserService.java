package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Injeção de dependência via Construtor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /** CRUD: salvar/alterar (Método Save) */
    public User save(User user) {
        // Futuramente: Adicionar lógica de validação e criptografia de senha (security)
        return userRepository.save(user);
    }

    /** CRUD: listar (Método FindAll) */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /** CRUD: buscarPorId (Método FindById) */
    public User findById(Integer id) {
        // Retorna o usuário ou lança uma exceção se não for encontrado (para retornar HTTP 404)
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    /** CRUD: excluir (Método Delete) */
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}