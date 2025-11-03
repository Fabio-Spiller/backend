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

    // ✅ Criar usuário
    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.setName(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getSenha());
        user.setRole(dto.getRole());
        userRepository.save(user);
        return new UserDTO(user);
    }


    public List<UserDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    public UserDTO buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
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

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        if (dto.getNome() != null) user.setName(dto.getNome());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getSenha() != null) user.setPassword(dto.getSenha());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        userRepository.save(user);
        return new UserDTO(user);
    }

    public void excluir(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }



    @Autowired
    private PasswordEncoder passwordEncoder; // Adicionando injeção do PasswordEncoder

    // Outros métodos ...

    public UserDTO criarPorAdmin(AdminCreateUserDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado: " + dto.getEmail());
        }

        User user = new User();
        user.setName(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getSenha()));
        user.setRole(Role.valueOf(dto.getRole().toUpperCase()));

        User salvo = userRepository.save(user);
        return new UserDTO(salvo);
    }
}

