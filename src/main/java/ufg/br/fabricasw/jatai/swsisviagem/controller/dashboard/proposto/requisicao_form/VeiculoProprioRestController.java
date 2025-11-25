package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form;

import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;

/**
 * 
 * @author Ronaldo N. de Sousa
 */
@RestController("requisicao_veiculo_proprio")
@RequestMapping("/dashboard/proposto")
@CrossOrigin("*")
public class VeiculoProprioRestController {
    
    @PostMapping("/requisicoes/veiculo_proprio")
    public ResponseEntity<VeiculoProprio> adicionarDocumentoVeiculoProprio(@RequestBody VeiculoProprio veiculoProprio, HttpSession sessao) {
        
        sessao.setAttribute(Constantes.SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO, true);
        sessao.setAttribute(Constantes.SESSION_DOC_VEICULO_PROPRIO, veiculoProprio);
        
        return ResponseEntity.ok().body(veiculoProprio);
    }
}
