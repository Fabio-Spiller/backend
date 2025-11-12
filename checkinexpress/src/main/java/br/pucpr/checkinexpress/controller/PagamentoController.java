package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.dto.PagamentoRegisterRequest;
import br.pucpr.checkinexpress.dto.PagamentoUpdateRequest;
import br.pucpr.checkinexpress.dto.PagamentoUpdateStatusRequest;
import br.pucpr.checkinexpress.model.Pagamento;
import br.pucpr.checkinexpress.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    // --- C: CREATE (Cria um novo registro de pagamento) ---
    // Apenas ADMIN e FUNCIONARIO podem registrar pagamentos (ex: no Check-out ou em pedidos de Serviço)
    @Secured({"ROLE_ADMIN", "ROLE_FUNCIONARIO"})
    @PostMapping
    public ResponseEntity<Pagamento> register(@RequestBody @Valid PagamentoRegisterRequest request) {
        Pagamento newPagamento = pagamentoService.register(request);
        return new ResponseEntity<>(newPagamento, HttpStatus.CREATED);
    }

    // --- R: READ (Lista todos os pagamentos) ---
    // Apenas ADMIN pode listar todos
    @Secured("ROLE_ADMIN")
    @GetMapping
    public ResponseEntity<List<Pagamento>> findAll() {
        return ResponseEntity.ok(pagamentoService.findAll());
    }

    // --- R: READ (Busca por ID) ---
    // ADMIN e FUNCIONARIO podem buscar
    @Secured({"ROLE_ADMIN", "ROLE_FUNCIONARIO"})
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> findById(@PathVariable Long id) {
        return ResponseEntity.ok(pagamentoService.findById(id));
    }

    // --- U: UPDATE DATA (Modificação de Valor e Tipo de Pagamento) ---
    // CORREÇÃO: Alterado de @PutMapping para @PatchMapping para suportar a requisição PATCH
    @Secured({"ROLE_ADMIN", "ROLE_FUNCIONARIO"})
    @PatchMapping("/{id}")
    public ResponseEntity<Pagamento> updateData(@PathVariable Long id,
                                                @RequestBody @Valid PagamentoUpdateRequest request) {
        Pagamento updatedPagamento = pagamentoService.updateData(id, request);
        return ResponseEntity.ok(updatedPagamento);
    }


    // --- U: UPDATE STATUS (Atualiza o status de PENDENTE para PAGO/CANCELADO) ---
    // Apenas ADMIN e FUNCIONARIO podem mudar o status
    @Secured({"ROLE_ADMIN", "ROLE_FUNCIONARIO"})
    @PutMapping("/{id}/status")
    public ResponseEntity<Pagamento> updateStatus(@PathVariable Long id,
                                                  @RequestBody @Valid PagamentoUpdateStatusRequest request) {
        Pagamento updatedPagamento = pagamentoService.updateStatus(id, request);
        return ResponseEntity.ok(updatedPagamento);
    }

    // --- D: DELETE ---
    // Apenas ADMIN pode deletar
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}