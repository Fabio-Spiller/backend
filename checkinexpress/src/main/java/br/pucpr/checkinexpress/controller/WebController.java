package br.pucpr.checkinexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Este Controller é responsável por servir todas as páginas HTML da aplicação.
 * Usamos @Controller em vez de @RestController porque queremos retornar
 * o nome de um template HTML, e não um JSON.
 */
@Controller
public class WebController {

    /**
     * Mapeia a rota raiz ("/") para o template "index.html".
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Mapeia a rota "/login" para o template "login.html".
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Mapeia a rota "/cadastro" para o template "cadastro.html".
     */
    @GetMapping("/cadastro")
    public String registerPage() {
        return "cadastro";
    }
}

