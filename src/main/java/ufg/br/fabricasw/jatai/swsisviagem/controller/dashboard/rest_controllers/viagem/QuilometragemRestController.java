package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.rest_controllers.viagem;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Quilometragem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoQuilometragem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.repository.QuilometragemRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 *
 * @author Ronaldo N Sousa
 *         Criado em: 29/07/2019
 */
@RestController
@RequestMapping(produces = "application/json; charset=UTF-8", value = "/dashboard/api/viagens/quilometragem")
@CrossOrigin("*")
public class QuilometragemRestController {

    @Autowired
    private QuilometragemRepository quilometragemRepository;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private ViagemService viagemService;

    @PostMapping("/{viagem_id}/{veiculo_id}")
    public ResponseEntity salvar(@RequestBody Quilometragem quilometragem,
            @PathVariable Long viagem_id, @PathVariable Long veiculo_id) {

        Viagem viagem = this.viagemService.find(viagem_id);
        Veiculo veiculo = this.veiculoService.findOne(veiculo_id);

        quilometragem.setViagem(viagem);
        quilometragem.setVeiculo(veiculo);

        quilometragem = this.quilometragemRepository.save(quilometragem);

        Map response = new HashMap();

        response.put("data", quilometragem);

        if (quilometragem.getTipoQuilometragem() == ETipoQuilometragem.KM_INICIAL) {

            viagem.getQuilometragemFinal().add(quilometragem);
            response.put("insercao_finalizada", viagem.hasQuilometragemFinalInserida());

        } else {

            viagem.getQuilometragemInicial().add(quilometragem);
            response.put("insercao_finalizada", viagem.hasQuilometragemInicialInserida());
        }

        return ResponseEntity.ok(response);
    }

}
