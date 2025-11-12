package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.ServicoRegisterRequest;
import br.pucpr.checkinexpress.dto.ServicoUpdateRequest;
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.Servico;
import br.pucpr.checkinexpress.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    // --- C: CREATE (Cria um novo serviço) ---
    @Transactional
    public Servico create(ServicoRegisterRequest request) {
        Optional<Servico> existingServico = servicoRepository.findByNome(request.getNome());
        if (existingServico.isPresent()) {
            throw new BusinessException("Serviço com este nome já cadastrado: " + request.getNome());
        }

        Servico servico = new Servico();
        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setPreco(request.getPreco());

        return servicoRepository.save(servico);
    }

    // --- R: READ (Busca todos os serviços) ---
    public List<Servico> findAll() {
        return servicoRepository.findAll();
    }

    // --- R: READ (Busca serviço por ID) ---
    public Servico findById(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + id));
    }

    // --- U: UPDATE (Atualiza um serviço existente) ---
    @Transactional
    public Servico update(Long id, ServicoUpdateRequest request) {
        Servico servico = findById(id);

        // Verificação se o novo nome já existe e pertence a outro ID
        Optional<Servico> existingServico = servicoRepository.findByNome(request.getNome());
        if (existingServico.isPresent() && !existingServico.get().getId().equals(id)) {
            throw new BusinessException("Já existe outro serviço com este nome: " + request.getNome());
        }

        servico.setNome(request.getNome());
        servico.setDescricao(request.getDescricao());
        servico.setPreco(request.getPreco());

        return servicoRepository.save(servico);
    }

    // --- D: DELETE (Exclui um serviço) ---
    @Transactional
    public void delete(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado com ID: " + id);
        }
        servicoRepository.deleteById(id);
    }
}