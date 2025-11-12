package br.pucpr.checkinexpress.controller;

import br.pucpr.checkinexpress.model.TipoQuarto;
import br.pucpr.checkinexpress.service.TipoQuartoService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-quarto")
public class TipoQuartoController {

    private final TipoQuartoService tipoQuartoService;

    public TipoQuartoController(TipoQuartoService tipoQuartoService) {
        this.tipoQuartoService = tipoQuartoService;
    }

    // 🔹 Listar todos
    @Secured("ROLE_ADMIN")
    @GetMapping
    public List<TipoQuarto> listarTodos() {
        return tipoQuartoService.listarTodos();
    }

    // 🔹 Buscar por ID
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    public TipoQuarto buscarPorId(@PathVariable Long id) {
        return tipoQuartoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Tipo de quarto não encontrado"));
    }

    // 🔹 Criar tipo de quarto
    @Secured("ROLE_ADMIN")
    @PostMapping
    public TipoQuarto salvar(@RequestBody TipoQuarto tipoQuarto) {
        return tipoQuartoService.salvar(tipoQuarto);
    }

    // 🔹 Atualizar tipo de quarto
    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public TipoQuarto atualizar(@PathVariable Long id, @RequestBody TipoQuarto tipoQuarto) {
        return tipoQuartoService.atualizar(id, tipoQuarto);
    }

    // 🔹 Deletar tipo de quarto
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        tipoQuartoService.deletar(id);
    }
}
