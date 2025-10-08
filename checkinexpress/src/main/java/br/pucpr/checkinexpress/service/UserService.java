package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.CreateUserRequest;
import br.pucpr.checkinexpress.dto.UserResponse;
import br.pucpr.checkinexpress.model.Role;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção de dependências: O Spring nos fornece as instâncias necessárias.
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo usuário no sistema.
     * @param request Os dados do novo usuário.
     * @return Os dados do usuário criado.
     */
    public UserResponse create(CreateUserRequest request) {
        // Regra de negócio: Verifica se o e-mail já está em uso
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            // Lançamos uma exceção que poderá ser tratada pelo Controller.
            throw new IllegalStateException("O e-mail informado já está em uso.");
        }

        // Cria uma nova instância da entidade User
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());
        user.setCpf(request.getCpf());

        // Criptografa a senha antes de salvar
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        // Todo novo usuário é criado com o papel 'USER'
        user.setRole(Role.USER);

        // Salva o usuário no banco de dados
        User savedUser = userRepository.save(user);

        // Retorna um DTO de resposta com os dados do usuário salvo
        return new UserResponse(savedUser);
    }
}
