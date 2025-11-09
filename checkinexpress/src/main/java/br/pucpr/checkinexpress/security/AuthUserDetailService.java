package br.pucpr.checkinexpress.security;

import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.pucpr.checkinexpress.security.UserAuthentication; // IMPORTA SUA CLASSE DE AUTENTICAÇÃO
import br.pucpr.checkinexpress.security.Role;

@Service
public class AuthUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o User pelo email no banco de dados
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Mapeia a sua Entidade 'User' para a classe de segurança 'UserAuthentication'
        UserAuthentication auth = new UserAuthentication();
        auth.setId(user.getId());
        auth.setEmail(user.getEmail());
        auth.setPassword(user.getSenha());

        // **IMPORTANTE**: Você precisa ter o campo 'Role' na sua entidade User para usar esta linha
        // auth.setRole(user.getRole());
        // Se ainda não tem, defina uma Role padrão temporariamente:
        auth.setRole(Role.USER); // Assumindo que Role.USER existe

        return auth;
    }
}