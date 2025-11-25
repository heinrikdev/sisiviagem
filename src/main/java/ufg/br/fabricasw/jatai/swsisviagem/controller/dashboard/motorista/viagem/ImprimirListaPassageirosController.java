package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.motorista.viagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * 
 * @author ronogue
 */
@Controller
@RequestMapping("/dashboard/viagem/imprimir_lista_passageiros")
@PreAuthorize("hasAuthority('MOTORISTA')")
public class ImprimirListaPassageirosController {
    
    private static final String VIEW_PRINT_PASSAGEIROS_VIAGEM = "app/print_to_pdf/viagem/print_passageiros";
    private static final String ATTR_VIAGEM = "viagem";
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String ATTR_MOTORISTAS = "motoristas";
    
    @Autowired
    private ViagemService viagemService;
    
    @GetMapping("/{viagem_id}")
    public String index(Model model, @PathVariable("viagem_id") Long viagem_id) {
        
        Viagem viagem = this.viagemService.find(viagem_id);
        
        model.addAttribute(ATTR_VIAGEM, viagem);
        model.addAttribute(ATTR_MOTORISTAS, viagem.getMotoristas());
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS, viagem.getRequisicaoPassageiros());
        
        return VIEW_PRINT_PASSAGEIROS_VIAGEM;
    }
}
