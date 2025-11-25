package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("viagem_agenda")
@RequestMapping("/dashboard/viagens/agenda")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class AgendaController {

    private static final String AGENDA_PAGE_VIEW = "app/dashboard/transporte/viagem/agenda";

    @GetMapping
    public String index() {
        return AGENDA_PAGE_VIEW;
    }
}
