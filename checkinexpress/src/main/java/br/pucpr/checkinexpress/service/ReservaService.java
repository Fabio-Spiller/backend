package br.pucpr.checkinexpress.service;

import br.pucpr.checkinexpress.model.Reserva;
import br.pucpr.checkinexpress.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada!"));
    }

    public Reserva criar(Reserva reserva) {
        return reservaRepository.save(reserva);
    }

    public Reserva atualizar(Long id, Reserva reservaAtualizada) {
        Reserva reserva = buscarPorId(id);

        reserva.setDataCheckin(reservaAtualizada.getDataCheckin());
        reserva.setDataCheckout(reservaAtualizada.getDataCheckout());
        reserva.setStatus(reservaAtualizada.getStatus());
        reserva.setUsuario(reservaAtualizada.getUsuario());
        reserva.setQuarto(reservaAtualizada.getQuarto());
        reserva.setServico(reservaAtualizada.getServico());
        reserva.setPagamento(reservaAtualizada.getPagamento());

        return reservaRepository.save(reserva);
    }

    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }
}
