package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Este método é chamado pelo Spring Security durante a tentativa de login.
     * Ele busca o usuário pelo email (que estamos tratando como username).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Usa nosso UserRepository para encontrar o usuário no banco de dados.
        // Se não encontrar, lança uma exceção que o Spring Security entende.
        return (UserDetails) userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email: " + username));
    }
}
