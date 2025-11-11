package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest; // NOVO IMPORT
import br.pucpr.checkinexpress.dto.LoginRequest; // NOVO
import br.pucpr.checkinexpress.dto.LoginResponse;
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.Role;
import br.pucpr.checkinexpress.security.JwtService; // NOVO
import br.pucpr.checkinexpress.security.UserAuthentication;

import org.springframework.security.authentication.AuthenticationManager; // NOVO
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // NOVO
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // NOVO IMPORT
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // NOVO
    private final JwtService jwtService; // NOVO

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // --- C: CREATE (Método registerUser já existente) ---
    public User registerUser(UserRegisterRequest request) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getSenha());
        user.setSenha(encodedPassword);

        user.setRole(Role.HOSPEDE);

        return userRepository.save(user);
    }

    // --- LOGIN: Autentica e Gera o Token JWT ---
    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {

        // 1. AUTENTICAÇÃO:
        // Lança exceção (AuthenticationException) se o usuário/senha estiver errado
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // 2. RECUPERAÇÃO DOS DETALHES DO USUÁRIO:
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. GERAÇÃO DO TOKEN JWT:
        String jwtToken = jwtService.generateToken(userDetails);

        // 4. PREPARAÇÃO DA RESPOSTA:
        String role = "";
        String email = userDetails.getUsername();

        // Se o objeto for a nossa implementação, pegamos a role
        if (userDetails instanceof UserAuthentication) {
            role = ((UserAuthentication) userDetails).getRole().name();
        } else {
            // Caso não seja, buscamos a role (depende da sua implementação, mas UserAuthentication é o padrão)
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado após autenticação."));
            role = user.getRole().name();
        }


        // Retorna o token, a role e o email
        return new LoginResponse(jwtToken, role, email);
    }

    // ----------------------------------------------------------------------------------

    // --- R: READ (Ler o perfil pelo ID) ---
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