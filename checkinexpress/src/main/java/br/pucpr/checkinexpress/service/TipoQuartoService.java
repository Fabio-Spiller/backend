package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.model.TipoQuarto;
import br.pucpr.checkinexpress.repository.TipoQuartoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoQuartoService {

    private final TipoQuartoRepository tipoQuartoRepository;

    public TipoQuartoService(TipoQuartoRepository tipoQuartoRepository) {
        this.tipoQuartoRepository = tipoQuartoRepository;
    }

    public List<TipoQuarto> listarTodos() {
        return tipoQuartoRepository.findAll();
    }

    public Optional<TipoQuarto> buscarPorId(Long id) {
        return tipoQuartoRepository.findById(id);
    }

    public TipoQuarto salvar(TipoQuarto tipoQuarto) {
        return tipoQuartoRepository.save(tipoQuarto);
    }

    public TipoQuarto atualizar(Long id, TipoQuarto novoTipo) {
        return tipoQuartoRepository.findById(id)
                .map(tipo -> {
                    tipo.setNome(novoTipo.getNome());
                    tipo.setDescricao(novoTipo.getDescricao());
                    tipo.setValorDiaria(novoTipo.getValorDiaria());
                    return tipoQuartoRepository.save(tipo);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de quarto não encontrado"));
    }

    public void deletar(Long id) {
        if (!tipoQuartoRepository.existsById(id)) {
            throw new RuntimeException("Tipo de quarto não encontrado");
        }
        tipoQuartoRepository.deleteById(id);
    }
}
