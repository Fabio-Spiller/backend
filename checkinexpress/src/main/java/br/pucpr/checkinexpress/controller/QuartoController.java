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

    // 🔹 Listar todos
    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<Quarto> listarTodos() {
        return quartoService.listarTodos();
    }

    // 🔹 Buscar por ID
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable Long id) {
        return quartoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
    }

    // 🔹 Criar novo quarto
    @Secured("ROLE_ADMIN")
    @PostMapping
    public Quarto salvar(@RequestBody Quarto quarto) {
        return quartoService.salvar(quarto);
    }

    // 🔹 Atualizar quarto existente
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public Quarto atualizar(@PathVariable Long id, @RequestBody Quarto quarto) {
        return quartoService.atualizar(id, quarto);
    }

    // 🔹 Deletar quarto
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        quartoService.deletar(id);
    }
}
