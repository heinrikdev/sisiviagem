package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.veiculo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoService;

/**
 * Classe responsável por cadastrar e atualizar as informações de um veículo.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_veiculo_form")
@RequestMapping("/dashboard/transporte/veiculo")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class FormularioVeiculoController {

    private static final String TITULO_NOVO_VEICULO = "Adicionar Veículo";
    private static final String TITULO_EDIT_VEICULO = "Editar Veículo";

    private static final String VIEW_FORMULARIO_VEICULO = "app/dashboard/transporte/veiculo/formulario";
    private static final String ATTR_VEICULO = "veiculo";

    private static final String REDIRECT_LINK_LISTAR_VEICULO = "redirect:/dashboard/transporte/veiculo/listar";

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping
    public String index(Model model) {

        Veiculo veiculo = new Veiculo();

        model.addAttribute(ATTR_VEICULO, veiculo);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_NOVO_VEICULO);

        return VIEW_FORMULARIO_VEICULO;
    }

    @GetMapping("/{veiculo_id}")
    public String indexEditar(Model model, @PathVariable("veiculo_id") Long veiculo_id) {

        Veiculo veiculo = this.veiculoService.findOne(veiculo_id);

        model.addAttribute(ATTR_VEICULO, veiculo);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_EDIT_VEICULO);

        return VIEW_FORMULARIO_VEICULO;
    }

    @GetMapping("/alternar_status/{veiculo_id}/{status_atual}")
    public String alterarStatus(@PathVariable("veiculo_id") Long id,
            @PathVariable("status_atual") Boolean status_atual) {

        this.veiculoService.alternarStatus(id, status_atual);
        return REDIRECT_LINK_LISTAR_VEICULO;
    }

    /**
     * Salva/Atualiza o veiculo no banco de dados.
     * 
     * @param veiculo
     * @return
     */
    @PostMapping
    public String submitFormulario(Veiculo veiculo) {

        this.veiculoService.save(veiculo);
        return REDIRECT_LINK_LISTAR_VEICULO;
    }
}
