package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoUsuario;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

/**
 * Durante a viagem pode haver o abastecimento do veículo.
 *
 * @author Ronaldo N. de Sousa
 */
@Controller("transporte_viagem_abastecimento")
@RequestMapping("/dashboard/transporte/viagem")
@PreAuthorize("hasAuthority('TRANSPORTE') || hasAuthority('MOTORISTA')")
public class AbastecimentoController {

    private final static String REDIRECT_LINK_VIAGEM_DETALHES = "redirect:/dashboard/transporte/viagem/${viagem_id}/detalhes";
    private final static String REDIRECT_LINK_VIAGEM_DETALHES_MOTORISTA = "redirect:/dashboard/motorista/viagem/detalhes/";
    @Autowired
    private ViagemService viagemService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/adicionar_abastecimento/{viagem_id}")
    public String adicionarAbastecimento(@PathVariable("viagem_id") Long viagem_id,
            @RequestParam("veiculo_id") Long veiculo_id, Abastecimento abastecimento) {

        Viagem viagem = this.viagemService.find(viagem_id);
        Veiculo veiculo = this.buscarVeiculo(viagem, veiculo_id);

        abastecimento.setVeiculo(veiculo);

        if (abastecimento.getId() == null && veiculo != null) {

            this.adicionarAbastecimento(viagem, abastecimento);

        } else if (veiculo != null) {

            this.atualizarAbastecimento(viagem, abastecimento);
        }

        return this.redirecionar(viagem);
    }

    @GetMapping("/remover_abastecimento/{viagem_id}/{abastecimento_id}")
    public String removerAbastecimento(@PathVariable("viagem_id") Long viagem_id,
            @PathVariable("abastecimento_id") Long abastecimento_id) {

        Viagem viagem = this.viagemService.find(viagem_id);
        Optional<Abastecimento> abastecimento = viagem.getAbastecimentos().stream()
                .filter((abs) -> abs.getId().equals(abastecimento_id)).findFirst();

        if (abastecimento.isPresent()) {

            viagem.getAbastecimentos().remove(abastecimento.get());
            this.viagemService.save(viagem);
        }

        return this.redirecionar(viagem);
    }

    private void adicionarAbastecimento(Viagem viagem, Abastecimento abastecimento) {

        viagem.getAbastecimentos().add(abastecimento);
        this.viagemService.atualizar(viagem);
    }

    private void atualizarAbastecimento(Viagem viagem, Abastecimento abastecimento) {

        viagem.getAbastecimentos().remove(abastecimento);
        viagem.getAbastecimentos().add(abastecimento);

        this.viagemService.atualizar(viagem);
    }

    private String redirecionar(Viagem viagem) {

        ETipoUsuario tipoUsuario = this.usuarioService.findUserLogado().getTipoUsuario();

        if (tipoUsuario.equals(ETipoUsuario.Motorista)) {

            StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES_MOTORISTA);
            builder.append(viagem.getId().toString());

            final String REDIRECT_VIAGEM_DETALHES = builder.toString();
            return REDIRECT_VIAGEM_DETALHES;
        } else {

            StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES);
            builder.replace(38, 50, viagem.getId().toString());

            final String REDIRECT_VIAGEM_DETALHES = builder.toString();
            return REDIRECT_VIAGEM_DETALHES;
        }
    }

    private Veiculo buscarVeiculo(Viagem viagem, Long veiculo_id) {

        Optional<Veiculo> findFirst = viagem.getVeiculos().stream()
                .filter((veiculo) -> veiculo.getId().equals(veiculo_id)).findFirst();

        if (findFirst.isPresent()) {
            return findFirst.get();
        }

        return null;
    }
}
