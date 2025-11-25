package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.service.AnexoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;
import ufg.br.fabricasw.jatai.swsisviagem.storage.FileSystemStorageService;

/**
 * Para registro pode ser anexado documentos a viagem.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller
@RequestMapping("/dashboard/transporte/viagem")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class AnexoController {

    private static final String REDIRECT_LINK_VIAGEM_DETALHES = "redirect:/dashboard/transporte/viagem/${viagem_id}/detalhes";

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private AnexoService anexoService;

    @Autowired
    private FileSystemStorageService storageService;

    @PostMapping("/adicionar_documento/{viagem_id}")
    public String adicionarAnexo(@PathVariable("viagem_id") Long viagem_id, Anexo anexo, BindingResult validation) {

        ArrayList<Anexo> list = new ArrayList<>();
        list.add(anexo);

        anexo.setRotulo("Doc. Viagem");

        this.storageService.store(list);

        Viagem viagem = this.viagemService.find(viagem_id);
        viagem.getAnexos().add(anexo);

        this.viagemService.atualizar(viagem);

        StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES);
        builder.replace(38, 50, viagem.getId().toString());

        return builder.toString();
    }

    @GetMapping("/remover_documento/{viagem_id}/{anexo_id}")
    public String removerAnexo(@PathVariable("anexo_id") Long anexo_id, @PathVariable("viagem_id") Long viatem_id) {

        Anexo anexo = this.anexoService.find(anexo_id);

        this.storageService.deletar(anexo);
        this.anexoService.deleteAnexoViagem(anexo_id);

        StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES);
        builder.replace(38, 50, viatem_id.toString());

        return builder.toString();
    }
}
