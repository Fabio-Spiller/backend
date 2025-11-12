package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.model.Quarto;
import br.pucpr.checkinexpress.model.TipoQuarto;
import br.pucpr.checkinexpress.repository.QuartoRepository;
import br.pucpr.checkinexpress.repository.TipoQuartoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuartoService {

    private final QuartoRepository quartoRepository;
    private final TipoQuartoRepository tipoQuartoRepository;

    public QuartoService(QuartoRepository quartoRepository, TipoQuartoRepository tipoQuartoRepository) {
        this.quartoRepository = quartoRepository;
        this.tipoQuartoRepository = tipoQuartoRepository;
    }

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    public Optional<Quarto> buscarPorId(Long id) {
        return quartoRepository.findById(id);
    }

    public Quarto salvar(Quarto quarto) {
        // Valida se o TipoQuarto existe no banco
        if (quarto.getTipo() != null && quarto.getTipo().getId() != 0) {
            TipoQuarto tipo = tipoQuartoRepository.findById((long) quarto.getTipo().getId())
                    .orElseThrow(() -> new RuntimeException("Tipo de quarto não encontrado"));
            quarto.setTipo(tipo);
        } else {
            throw new RuntimeException("O campo 'tipo.id' é obrigatório");
        }
        return quartoRepository.save(quarto);
    }

    public Quarto atualizar(Long id, Quarto novoQuarto) {
        return quartoRepository.findById(id).map(q -> {
            q.setNumero(novoQuarto.getNumero());
            q.setAndar(novoQuarto.getAndar());
            q.setRole(novoQuarto.getRole());

            if (novoQuarto.getTipo() != null && novoQuarto.getTipo().getId() != 0) {
                TipoQuarto tipo = tipoQuartoRepository.findById((long) novoQuarto.getTipo().getId())
                        .orElseThrow(() -> new RuntimeException("Tipo de quarto não encontrado"));
                q.setTipo(tipo);
            }
            return quartoRepository.save(q);
        }).orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
    }

    public void deletar(Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new RuntimeException("Quarto não encontrado");
        }
        quartoRepository.deleteById(id);
    }
}
