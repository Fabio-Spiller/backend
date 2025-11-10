package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest; // NOVO IMPORT
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.Role;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // NOVO IMPORT
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- C: CREATE (Método registerUser já existente) ---
    public User registerUser(UserRegisterRequest request) {
        // ... (código de registro existente) ...
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getSenha());
        user.setSenha(encodedPassword);

        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // ----------------------------------------------------------------------------------

    // --- R: READ (Ler o perfil pelo ID) ---
    // Este método será usado para o usuário logado ver seus próprios dados.
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + id));
    }

    // ----------------------------------------------------------------------------------

    // --- U: UPDATE (Atualizar nome e/ou senha) ---
    public User update(Long id, UserUpdateRequest request) {
        // 1. Busca o usuário existente
        User user = findById(id); // Reusa o método findById para garantir que o usuário existe

        // 2. Atualiza o nome
        user.setNome(request.getNome());

        // 3. Verifica e atualiza a senha, se fornecida
        if (request.getNovaSenha() != null && !request.getNovaSenha().isBlank()) {
            // Encripta a nova senha antes de salvar
            String encodedPassword = passwordEncoder.encode(request.getNovaSenha());
            user.setSenha(encodedPassword);
        }

        // 4. Salva as alterações
        return userRepository.save(user);
    }

    // ----------------------------------------------------------------------------------

    // --- D: DELETE (Excluir conta) ---
    public void delete(Long id) {
        // 1. Verifica se o usuário existe antes de tentar deletar
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuário não encontrado para exclusão com ID: " + id);
        }

        // 2. Exclui a conta
        userRepository.deleteById(id);
    }
}