package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.model.Quarto;
import br.pucpr.checkinexpress.repository.QuartoRepository;
import br.pucpr.checkinexpress.repository.TipoQuartoRepository;
import br.pucpr.checkinexpress.repository.UserRepository;
import br.pucpr.checkinexpress.security.Role_disponivel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService {

    private final QuartoRepository quartoRepository;
    private final TipoQuartoRepository tipoQuartoRepository;
    private final UserRepository userRepository;

    public QuartoService(QuartoRepository quartoRepository,
                         TipoQuartoRepository tipoQuartoRepository,
                         UserRepository userRepository) {
        this.quartoRepository = quartoRepository;
        this.tipoQuartoRepository = tipoQuartoRepository;
        this.userRepository = userRepository;
    }

    public List<Quarto> listar() {
        return quartoRepository.findAll();
    }

    public Quarto buscarPorId(int id) {
        return quartoRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado!"));
    }

    public Quarto criar(Quarto quarto) {
        tipoQuartoRepository.findById((long) quarto.getTipo().getId())
                .orElseThrow(() -> new RuntimeException("Tipo de quarto inválido!"));

        if (quarto.getHospede() != null) {
            userRepository.findById(quarto.getHospede().getId())
                    .orElseThrow(() -> new RuntimeException("Hóspede inválido!"));
        }

        return quartoRepository.save(quarto);
    }
    public Quarto atualizar(int id, Quarto quartoAtualizado) {
        Quarto quarto = buscarPorId(id);

        quarto.setNumero(quartoAtualizado.getNumero());
        quarto.setAndar(quartoAtualizado.getAndar());
        quarto.setRole(quartoAtualizado.getRole());
        quarto.setTipo(quartoAtualizado.getTipo());
        quarto.setHospede(quartoAtualizado.getHospede());

        return quartoRepository.save(quarto);
    }

    public void deletar(int id) {
        quartoRepository.deleteById((long)id);
    }

    public Quarto ocupar(int id, int idHospede) {
        Quarto quarto = buscarPorId(id);
        quarto.setHospede(userRepository.findById((long) idHospede)
                .orElseThrow(() -> new RuntimeException("Hóspede não encontrado!")));
        quarto.setRole(Role_disponivel.ocupado);
        return quartoRepository.save(quarto);
    }

    public Quarto liberar(int id) {
        Quarto quarto = buscarPorId(id);
        quarto.setHospede(null);
        quarto.setRole(Role_disponivel.livre);
        return quartoRepository.save(quarto);
    }
}