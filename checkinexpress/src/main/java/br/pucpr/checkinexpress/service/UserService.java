package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.UserDTO;
import br.pucpr.checkinexpress.model.User;
import br.pucpr.checkinexpress.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setSenha(userDTO.getSenha());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return new UserDTO(user);
    }

    public UserDTO atualizar(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setSenha(userDTO.getSenha());
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
}
