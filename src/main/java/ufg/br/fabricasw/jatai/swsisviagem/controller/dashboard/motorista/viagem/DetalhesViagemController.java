package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.motorista.viagem;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("motorista_viagem_detalhes")
@RequestMapping("/dashboard/motorista/viagem")
@PreAuthorize("hasAuthority('MOTORISTA')")
public class DetalhesViagemController {

    private static final String TITULO_PAGINA_DETALHES_VIAGEM = "Informações da Viagem ";
    private static final String VIEW_DETALHES_VIAGEM = "app/dashboard/motorista/viagem/detalhes";
    private static final String VIEW_TROCAR_VEICULO_VIAGEM = "app/dashboard/motorista/viagem/trocar_veiculo";
    private static final String ATTR_LISTA_VEICULOS_PARA_ADD = "veiculos";
    private static final String ATTR_VIAGEM = "viagem";
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";
    private static final String ATTR_ABASTECIMENTO = "abastecimento";

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private VeiculoService veiculoService;

    @GetMapping("/detalhes/{viagem_id}")
    public String index(Model model, @PathVariable("viagem_id") Long viagem_id) {

        Viagem viagem = this.viagemService.find(viagem_id);

        StringBuilder builder = new StringBuilder(TITULO_PAGINA_DETALHES_VIAGEM);
        builder.append(viagem.getId().toString());

        final String TITULO_PAGINA = builder.toString();

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGINA);
        model.addAttribute(ATTR_VIAGEM, viagem);
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS, viagem.getRequisicaoPassageiros());
        model.addAttribute(ATTR_ABASTECIMENTO, new Abastecimento());

        return VIEW_DETALHES_VIAGEM;
    }

    @GetMapping("/detalhes/trocarVeiculo/{viagem_id}/{veiculo_rm_id}")
    public String trocarVeiculo(Model model, @PathVariable("viagem_id") Long viagem_id,
            @PathVariable("veiculo_rm_id") Long veiculo_rm_id) {

        Viagem viagem = this.viagemService.find(viagem_id);
        List<Veiculo> veiculos;

        veiculos = this.veiculoService.findAllVeiculoExcetoNaViagem(viagem.getId());

        model.addAttribute(ATTR_LISTA_VEICULOS_PARA_ADD, veiculos);
        model.addAttribute(ATTR_VIAGEM, viagem);
        model.addAttribute("veiculo_rm_id", veiculo_rm_id);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, "Trocar Veículo");

        return VIEW_TROCAR_VEICULO_VIAGEM;
    }

    @GetMapping("/detalhes/trocarVeiculo/{veiculo_add_id}/{veiculo_rm_id}/{viagem_id}")
    public String trocarVeiculo(Model model, @PathVariable("veiculo_add_id") Long veiculo_add_id,
            @PathVariable("veiculo_rm_id") Long veiculo_rm_id, @PathVariable("viagem_id") Long viagem_id) {

        Viagem viagem = this.viagemService.find(viagem_id);
        Veiculo veiculo = this.veiculoService.findOne(veiculo_add_id);
        viagem.getVeiculos().add(veiculo);

        this.viagemService.save(viagem);

        this.viagemService.deleteVeiculoViagem(viagem_id, veiculo_rm_id);

        return "redirect:/dashboard/motorista/viagem/detalhes/" + viagem_id;
    }

    @DeleteMapping("/detalhes/deleteVeiculo/{viagem_id}/{veiculo_id}")
    public String deletarVeiculo(@PathVariable("viagem_id") Long viagem_id,
            @PathVariable("veiculo_id") Long veiculo_id) {
        this.viagemService.deleteVeiculoViagem(viagem_id, veiculo_id);
        return VIEW_DETALHES_VIAGEM;
    }

    @GetMapping("/detalhes/iniciar/{viagem_id}")
    public String iniciarViagem(@PathVariable("viagem_id") Long viagem_id) {

        Viagem viagem = this.viagemService.find(viagem_id);
        viagem.setEstadoViagem(EEstadoViagem.Em_andamento);

        this.viagemService.atualizar(viagem);
        return "redirect:/dashboard/motorista/viagem/detalhes/" + viagem_id;
    }

    @PostMapping("/detalhes/encerrar/{viagem_id}")
    public String encerrarViagem(@PathVariable("viagem_id") Long viagem_id,
            @RequestParam(name = "observacao", required = false) String observacao) {

        Viagem viagem = this.viagemService.find(viagem_id);
        viagem.setEstadoViagem(EEstadoViagem.Encerrada);
        viagem.setObservacao(observacao);

        viagem.getRequisicaos().stream().forEach((Requisicao requisicao) -> requisicao.getSolicitacaoTransporte()
                .setEstadoSolicitacao(EEstadoSolicitacao.ENCERRADA));

        this.viagemService.atualizar(viagem);
        return "redirect:/dashboard/motorista/viagem/detalhes/" + viagem_id;
    }
}
