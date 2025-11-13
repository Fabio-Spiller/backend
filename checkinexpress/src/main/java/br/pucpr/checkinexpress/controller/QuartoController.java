package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.Quarto;
import br.pucpr.checkinexpress.service.QuartoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<Quarto> listarQuartos() {
        return quartoService.listar();
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable long id) {
        return quartoService.buscarPorId(id);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public Quarto criarQuarto(@RequestBody Quarto quarto) {
        return quartoService.criar(quarto);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public Quarto atualizarQuarto(@PathVariable long id, @RequestBody Quarto quartoAtualizado) {
        return quartoService.atualizar(id, quartoAtualizado);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deletarQuarto(@PathVariable long id) {
        quartoService.deletar(id);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}/ocupar/{idHospede}")
    public Quarto ocuparQuarto(@PathVariable long id, @PathVariable int idHospede) {
        return quartoService.ocupar(id, idHospede);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}/liberar")
    public Quarto liberarQuarto(@PathVariable long id) {
        return quartoService.liberar(id);
    }
}