package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.dto.PagamentoRegisterRequest;
import br.pucpr.checkinexpress.dto.PagamentoUpdateRequest;
import br.pucpr.checkinexpress.dto.PagamentoUpdateStatusRequest;
import br.pucpr.checkinexpress.exception.BusinessException;
import br.pucpr.checkinexpress.model.Pagamento;
import br.pucpr.checkinexpress.model.StatusPagamento;
import br.pucpr.checkinexpress.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    // --- C: CREATE ---
    @Transactional
    public Pagamento register(PagamentoRegisterRequest request) {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(request.getValor());
        pagamento.setTipoPagamento(request.getTipoPagamento());
        pagamento.setReservaId(request.getReservaId());

        // Pagamento começa sempre como PENDENTE por padrão
        pagamento.setStatus(StatusPagamento.PENDENTE);

        return pagamentoRepository.save(pagamento);
    }

    // --- R: READ ---
    public Pagamento findById(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Pagamento não encontrado com o ID: " + id));
    }

    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

    // --- U: UPDATE DATA (Modificação de Valor e Tipo de Pagamento) ---
    @Transactional
    public Pagamento updateData(Long id, PagamentoUpdateRequest request) {
        Pagamento pagamento = findById(id);

        // Regra de Negócio: Só permite alterar o valor ou tipo de pagamento se estiver PENDENTE.
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Não é possível alterar dados (valor/tipo) de um pagamento que não está PENDENTE.");
        }

        // Atualiza os dados
        pagamento.setValor(request.getValor());
        pagamento.setTipoPagamento(request.getTipoPagamento());

        // Salva e retorna a entidade atualizada
        return pagamentoRepository.save(pagamento);
    }

    // --- U: UPDATE STATUS (Ação Administrativa) ---
    @Transactional
    public Pagamento updateStatus(Long id, PagamentoUpdateStatusRequest request) {
        Pagamento pagamento = findById(id);

        // Regra de Negócio: Não permite alterar o status de um pagamento CANCELADO para outro.
        if (pagamento.getStatus() == StatusPagamento.CANCELADO) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Não é possível alterar o status de um pagamento que já foi CANCELADO.");
        }

        pagamento.setStatus(request.getStatus());
        return pagamentoRepository.save(pagamento);
    }

    // --- D: DELETE ---
    @Transactional
    public void delete(Long id) {
        Pagamento pagamento = findById(id);

        // Regra de Negócio: Não permite excluir pagamentos que já estão pagos
        if (pagamento.getStatus() == StatusPagamento.PAGO) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Não é possível excluir um pagamento que já foi processado e está PAGO.");
        }

        pagamentoRepository.delete(pagamento);
    }
}