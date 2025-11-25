package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.repository.ViagemRepository;
import ufg.br.fabricasw.jatai.swsisviagem.repository.projection.ViagemSimpleProjection;

/**
 * RestController responsável por apresentar as viagens agendadas no formato
 * json.
 * 
 * @author Ronaldo N. de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/viagens")
@CrossOrigin("*")
public class ViagemRestController {

    @Autowired
    private ViagemRepository repository;

    @GetMapping
    public List<ViagemSimpleProjection> viagensAgendadas(@RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end) {
        return this.repository.findAllViagensAgendadas(start, end);
    }

}
