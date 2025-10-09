package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.AdminCreateUserDTO;
import br.pucpr.checkinexpress.dto.UserDTO;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserDTO(user);
    }

    public UserDTO salvar(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getSenha());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return new UserDTO(user);
    }

    public UserDTO atualizar(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setName(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getSenha());
        user.setRole(userDTO.getRole());
        userRepository.save(user);

        return new UserDTO(user);
    }

    public void excluir(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    // Exemplo em UserService.java


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Adicionando injeção do PasswordEncoder

    // Outros métodos ...

    public UserDTO criarPorAdmin(AdminCreateUserDTO dto) {
        // 1. Verifique se o email já existe (boa prática)

        // 2. Crie a entidade User
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // Lembre de injetar o passwordEncoder!

        // 3. Mapeie a Role
        user.setRole(Role.valueOf(dto.getRole())); // Converte a string "ADMIN" ou "USER" para o seu enum Role

        // 4. Salve no repositório
        User salvo = userRepository.save(user);

        // 5. Retorne o DTO de resposta
        return UserDTO.fromEntity(salvo); // Assumindo que você tem um método de mapeamento
    }
}

