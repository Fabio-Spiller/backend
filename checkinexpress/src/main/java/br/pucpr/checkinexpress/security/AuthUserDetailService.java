package br.pucpr.checkinexpress.security;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Usa o UserRepository para buscar o User pelo email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
        }

        User user = userOptional.get();

        // Mapeia sua entidade User para a classe de segurança UserAuthentication
        UserAuthentication auth = new UserAuthentication();
        auth.setId(user.getId().intValue());
        auth.setEmail(user.getEmail());
        auth.setPassword(user.getSenha());

        // **IMPORTANTE**: Você precisará definir o campo Role para este usuário.
        // Por enquanto, vamos definir uma Role padrão, mas ajuste isso com base no seu `User.java` e `Role.java`.
        // Assumindo que você tem um enum Role:
        auth.setRole(Role.USER); // <--- AJUSTAR ISSO PARA COMO VOCÊ GERENCIA ROLES

        return auth;
    }
}
