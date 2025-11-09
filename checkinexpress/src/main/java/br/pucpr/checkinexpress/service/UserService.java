package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injeta o BCryptPasswordEncoder

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(UserRegisterRequest request) {
        // 1. Verifica se o email já está em uso
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }

        // 2. Cria a entidade User
        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());

        // 3. ENCRIPTA A SENHA antes de salvar!
        String encodedPassword = passwordEncoder.encode(request.getSenha());
        user.setSenha(encodedPassword);

        // Define a Role padrão (USER)
        user.setRole(Role.USER);

        // 4. Salva o usuário no banco
        return userRepository.save(user);
    }
}
