package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.Quarto;
import br.pucpr.checkinexpress.repository.QuartoRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quartos")
public class QuartoController {

    private final QuartoRepository quartoRepository;

    public QuartoController(QuartoRepository quartoRepository) {
        this.quartoRepository = quartoRepository;
    }

    // Somente ADMIN pode acessar
    @Secured("ROLE_ADMIN")
    @GetMapping("/quartos")
    public List<Quarto> listarQuartos() {
        return quartoRepository.findAll();
    }
}
