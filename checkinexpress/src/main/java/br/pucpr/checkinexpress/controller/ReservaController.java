package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.Reserva;
import br.pucpr.checkinexpress.service.ReservaService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<Reserva> listarReservas() {
        return reservaService.listar();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public Reserva buscarPorId(@PathVariable Long id) {
        return reservaService.buscarPorId(id);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public Reserva criarReserva(@RequestBody Reserva reserva) {
        return reservaService.criar(reserva);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public Reserva atualizarReserva(@PathVariable Long id, @RequestBody Reserva reservaAtualizada) {
        return reservaService.atualizar(id, reservaAtualizada);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deletarReserva(@PathVariable Long id) {
        reservaService.deletar(id);
    }
}
