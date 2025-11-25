package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbastecimentoRepository;

/**
 *
 * @author Ronaldo Nogueira de Sousa
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/viagens/abastecimentos")
@CrossOrigin("*")
public class AbastecimentoRestController {

    @Autowired
    private AbastecimentoRepository repository;

    @GetMapping("/byId/{abastecimento_id}")
    public ResponseEntity<Abastecimento> abastecimeto(@PathVariable("abastecimento_id") Long abastecimento_id) {

        Optional<Abastecimento> findById = this.repository.findById(abastecimento_id);

        if (findById.isPresent()) {
            return ResponseEntity.ok(findById.get());
        }

        return ResponseEntity.notFound().build();
    }
}
