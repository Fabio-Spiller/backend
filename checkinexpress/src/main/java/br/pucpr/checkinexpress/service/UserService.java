package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.LoginRequest;
import br.pucpr.checkinexpress.dto.LoginResponse;
import br.pucpr.checkinexpress.dto.UserRegisterRequest;
import br.pucpr.checkinexpress.dto.UserUpdateRequest;
import br.pucpr.checkinexpress.dto.FuncionarioUpdateRequest; // NOVO IMPORT NECESSÁRIO
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.Funcionario;
import br.pucpr.checkinexpress.model.Quarto;
import br.pucpr.checkinexpress.model.TipoQuarto;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.FuncionarioRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, FuncionarioRepository funcionarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // ----------------------------------------------------------------------
    // --- MÉTODOS DE CRIAÇÃO (REGISTRO) ---
    // ----------------------------------------------------------------------

    @Transactional
    // Rota pública: Cria um HÓSPEDE (Role padrão)
    public User registerUser(UserRegisterRequest request) {
        return saveNewUser(request, Role.HOSPEDE);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    public User atualizar(Long id, User novoUser) {
        return userRepository.findById(id).map(q -> {
            q.setEmail(novoUser.getEmail());
            q.setNome(novoUser.getNome());
            q.setRole(novoUser.getRole());
            q.setSenha(novoUser.getSenha());

            return userRepository.save(q);
        }).orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
    }

    public void deletar(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Tipo de quarto não encontrado");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    // Rota protegida (por ADMIN): Cria um FUNCIONÁRIO
    public User registerFuncionario(UserRegisterRequest request) {
        User user = saveNewUser(request, Role.FUNCIONARIO);

        // ** NOVO PASSO: Salvar dados específicos na tabela Funcionario **
        // Corrigido para inicializar com valores padrão caso o DTO de registro não os tenha.
        Funcionario funcionario = new Funcionario(user, LocalDate.now());
        // Ajustado para usar os getters do UserRegisterRequest caso você os tenha adicionado:
        // Se você não adicionou cargo e salario no UserRegisterRequest, estes serão os valores padrão
        funcionario.setCargo(request.getCargo() != null ? request.getCargo() : "Atendente");
        funcionario.setSalario(request.getSalario() != null ? request.getSalario() : 0.0);
        funcionarioRepository.save(funcionario);

        return user;
    }

    @Transactional
    // Rota protegida (por ADMIN): Cria um ADMIN
    public User registerAdmin(UserRegisterRequest request) {
        User user = saveNewUser(request, Role.ADMIN);

        // ** ADMINs também são funcionários (tem dados na tabela Funcionario) **
        // Corrigido para inicializar com valores padrão caso o DTO de registro não os tenha.
        Funcionario funcionario = new Funcionario(user, LocalDate.now());
        funcionario.setCargo(request.getCargo() != null ? request.getCargo() : "Administrador");
        funcionario.setSalario(request.getSalario() != null ? request.getSalario() : 0.0);
        funcionarioRepository.save(funcionario);

        return user;
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

    // Autenticação e Geração de Token (Sem alteração)
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

    // Leitura (READ): Busca o perfil pelo ID (Sem alteração)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com ID: " + id));
    }

    // Novo método de Leitura (READ): Lista usuários por Role (Sem alteração)
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
    // NOVO MÉTODO: Atualiza apenas os dados específicos do Funcionario (Cargo e Salário)
    public Funcionario updateFuncionarioDetails(Long userId, FuncionarioUpdateRequest request) {
        // 1. Verifica se o User existe
        User user = findById(userId);

        // 2. Verifica se o User tem Role de FUNCIONARIO ou ADMIN
        if (user.getRole() == Role.HOSPEDE) {
            throw new BusinessException("Acesso negado: Usuário não é um Funcionário ou Administrador.");
        }

        // 3. Busca o registro na tabela Funcionario
        Funcionario funcionario = funcionarioRepository.findByUsuarioId(userId)
                .orElseThrow(() -> new BusinessException("Registro de Funcionário não encontrado para o ID: " + userId));

        // 4. Aplica as atualizações
        funcionario.setCargo(request.getCargo());
        funcionario.setSalario(request.getSalario());

        // 5. Salva no repositório de Funcionário
        return funcionarioRepository.save(funcionario);
    }


    @Transactional
    // Exclusão (DELETE): Exclui a conta (User) e o registro do Funcionario, se existir.
    public void delete(Long id) {
        User user = findById(id); // Usa findById que lança exceção se não encontrar

        // 1. Se for FUNCIONARIO ou ADMIN, deve ter um registro na tab_funcionario. Removemos primeiro.
        if (user.getRole() == Role.FUNCIONARIO || user.getRole() == Role.ADMIN) {
            Optional<Funcionario> funcionario = funcionarioRepository.findByUsuarioId(id);
            // Se o registro de funcionário for encontrado, exclui ele primeiro
            funcionario.ifPresent(funcionarioRepository::delete);
        }

        // 2. Excluímos o registro na tabela User
        userRepository.deleteById(id);
    }
}