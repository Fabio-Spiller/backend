package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.ServicoRegisterRequest;
import br.pucpr.checkinexpress.dto.ServicoUpdateRequest;
import br.pucpr.checkinexpress.model.Servico;
import br.pucpr.checkinexpress.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    // --- C: CREATE (Criar Serviço) ---
    // Restrito apenas ao ADMIN
    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<Servico> create(@RequestBody @Valid ServicoRegisterRequest request) {
        Servico newServico = servicoService.create(request);
        return new ResponseEntity<>(newServico, HttpStatus.CREATED);
    }

    // --- R: READ (Listar todos os Serviços) ---
    // Acessível para todos os usuários autenticados (HOSPEDE, FUNCIONARIO, ADMIN)
    @GetMapping
    public ResponseEntity<List<Servico>> findAll() {
        List<Servico> servicos = servicoService.findAll();
        return ResponseEntity.ok(servicos);
    }

    // --- R: READ (Buscar Serviço por ID) ---
    // Acessível para todos os usuários autenticados
    @GetMapping("/{id}")
    public ResponseEntity<Servico> findById(@PathVariable Long id) {
        Servico servico = servicoService.findById(id);
        return ResponseEntity.ok(servico);
    }

    // --- U: UPDATE (Atualizar Serviço) ---
    // Restrito apenas ao ADMIN
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Servico> update(@PathVariable Long id, @RequestBody @Valid ServicoUpdateRequest request) {
        Servico updatedServico = servicoService.update(id, request);
        return ResponseEntity.ok(updatedServico);
    }

    // --- D: DELETE (Excluir Serviço) ---
    // Restrito apenas ao ADMIN
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}