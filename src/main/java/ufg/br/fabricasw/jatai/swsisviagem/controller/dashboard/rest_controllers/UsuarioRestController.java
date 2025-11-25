package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Controller rest para realizar operacoes via ajax.
 * 
 * @author Ronaldo N. de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/usuario")
@CrossOrigin("*")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login_unico/em_uso/{login_unico}")
    public Boolean loginUnicoEstaEmUso(@PathVariable("login_unico") String login_unico) {
        return this.usuarioService.loginUnicoEstaEmUso(login_unico);
    }
}
