package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.LoginRequest;
import br.pucpr.checkinexpress.dto.LoginResponse;
import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest;
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.JwtService;
import br.pucpr.checkinexpress.security.Role;
import br.pucpr.checkinexpress.security.UserAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ----------------------------------------------------------------------
    // --- MÉTODOS DE CRIAÇÃO (REGISTRO) ---
    // ----------------------------------------------------------------------

    // Rota pública: Cria um HÓSPEDE (Role padrão)
    public User registerUser(UserRegisterRequest request) {
        return saveNewUser(request, Role.HOSPEDE);
    }

    // Rota protegida (por ADMIN): Cria um FUNCIONÁRIO (Método faltante no seu envio)
    public User registerFuncionario(UserRegisterRequest request) {
        return saveNewUser(request, Role.FUNCIONARIO);
    }

    // Rota protegida (por ADMIN): Cria um ADMIN (Método faltante no seu envio)
    public User registerAdmin(UserRegisterRequest request) {
        return saveNewUser(request, Role.ADMIN);
    }

    // Método auxiliar para evitar duplicação de código e lidar com a validação
    private User saveNewUser(UserRegisterRequest request, Role role) {
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new BusinessException("Email já cadastrado.");
        }

        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());

        String encodedPassword = passwordEncoder.encode(request.getSenha());
        user.setSenha(encodedPassword);

        user.setRole(role); // Define a Role específica

        return userRepository.save(user);
    }

    // ----------------------------------------------------------------------
    // --- MÉTODOS DE AUTENTICAÇÃO E BUSCA ---
    // ----------------------------------------------------------------------

    // Autenticação e Geração de Token
    public LoginResponse authenticateAndGenerateToken(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserAuthentication userAuth = (UserAuthentication) authentication.getPrincipal();

        String token = jwtService.generateToken(userAuth);

        return new LoginResponse(
                token,
                userAuth.getRole().name(),
                userAuth.getUsername()
        );
    }

    // Leitura (READ): Busca o perfil pelo ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + id));
    }

    // Novo método de Leitura (READ): Lista usuários por Role (Método faltante no seu envio)
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    // ----------------------------------------------------------------------
    // --- MÉTODOS DE ATUALIZAÇÃO E EXCLUSÃO ---
    // ----------------------------------------------------------------------


    @Transactional
    // Atualização (UPDATE): Atualiza nome e/ou senha
    public User update(Long id, UserUpdateRequest request) {
        User user = findById(id);

        if (request.getNome() != null && !request.getNome().isBlank()) {
            user.setNome(request.getNome());
        }

        if (request.getNovaSenha() != null && !request.getNovaSenha().isBlank()) {
            String encodedPassword = passwordEncoder.encode(request.getNovaSenha());
            user.setSenha(encodedPassword);
        }

        return userRepository.save(user);
    }

    @Transactional
    // Exclusão (DELETE): Exclui a conta
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UsernameNotFoundException("Usuário não encontrado para exclusão com ID: " + id);
        }
        userRepository.deleteById(id);
    }


}