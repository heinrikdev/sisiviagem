package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.storage.StorageService;

/**
 * Quando o proposto presta contas no sistema, o mesmo é indagado se houver
 * alguma divergência entre o que foi planejado e executa em uma viagem, quando
 * isso ocorre o mesmo deve fornecer os motivos da divergência e em seguida add
 * novos anexos caso preciso, logo por fim efetuar a inserção dos documentos
 * para a prestação de contas.
 *
 * @author ronogue
 */
@Controller("requisicao_divergencia_justificativa")
@RequestMapping("dashboard/proposto/requisicao")
@PreAuthorize("hasAuthority('USER')")
public class RequisicaoDivergenciaJustificativaController {

    private static final String TITULO_ATUALIZAR_ANEXOS_DIVERGENCIA = "Atualização de documentos, se necessário.";
    private static final String VIEW_ATUALIZAR_ANEXOS_FORM = "app/dashboard/proposto/prestacao_contas/atualizar_anexos";

    private static final String ATTR_ANEXOS = "anexos";
    private static final String ATTR_ANEXO = "anexo";
    private static final String ATTR_FORM_VALIDATION = "formAnexoValidation";

    private static final String REDIRECT_LINK_PRESTACAO_CONTAS = "redirect:/dashboard/proposto/requisicao/${requisicao_id}/prestacao_contas";
    private static final String REDIRECT_LINK_ATUALIZAR_ANEXOS = "redirect:/dashboard/proposto/requisicao/prestacao_contas/divergencia";

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private StorageService fileSystemStorageService;

    /**
     * Quando há divergência entre o executado e o planejado em uma viagem é
     * atualizado os anexos da requisição pelo proposto.
     * 
     * @param model
     * @param sessao
     * @return
     */
    @GetMapping("/prestacao_contas/divergencia")
    public String indexForm(Model model, HttpSession sessao) {

        Requisicao requisicao = (Requisicao) sessao.getAttribute(Constantes.ATTR_REQUISICAO);

        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(ATTR_ANEXOS, requisicao.getAnexos());
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_ATUALIZAR_ANEXOS_DIVERGENCIA);

        sessao.setAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        sessao.setAttribute(ATTR_ANEXOS, requisicao.getAnexos());

        model.addAttribute(ATTR_ANEXO, new Anexo());
        model.addAttribute(ATTR_FORM_VALIDATION, new ValidationWrapper());

        return VIEW_ATUALIZAR_ANEXOS_FORM;
    }

    /**
     * Salva os anexos adicionais e redireciona para a página de prestação de
     * contas.
     * 
     * @param model
     * @param sessao
     * @return
     */
    @PostMapping("/prestacao_contas/divergencia")
    public String salvarDivergenciaJustificativaAnexosForm(Model model, HttpSession sessao) {

        Requisicao requisicao = (Requisicao) sessao.getAttribute(Constantes.ATTR_REQUISICAO);
        List<Anexo> anexos = (List<Anexo>) sessao.getAttribute(ATTR_ANEXOS);

        this.fileSystemStorageService.store(anexos);
        requisicao.setAnexos(anexos);
        this.requisicaoServiceImpl.atualizar(requisicao);

        final String REDIRECT_TO = REDIRECT_LINK_PRESTACAO_CONTAS.replace("${requisicao_id}",
                requisicao.getId().toString());
        return REDIRECT_TO;
    }

    @PostMapping("/prestacao_contas/divergencia/add_anexo")
    public String divergenciaJustificativaAddAnexo(Model model, @Valid Anexo anexo, BindingResult validation,
            HttpSession sessao) {

        Requisicao requisicao = (Requisicao) sessao.getAttribute(Constantes.ATTR_REQUISICAO);
        List<Anexo> anexos = (List<Anexo>) sessao.getAttribute(ATTR_ANEXOS);

        if (!validation.hasErrors()) {

            anexos.add(anexo);
            model.addAttribute(ATTR_ANEXO, new Anexo());
            model.addAttribute(ATTR_FORM_VALIDATION, new ValidationWrapper());

        } else {

            model.addAttribute(ATTR_ANEXO, anexo);
            model.addAttribute(ATTR_FORM_VALIDATION, new ValidationWrapper(validation));
        }

        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(ATTR_ANEXOS, anexos);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_ATUALIZAR_ANEXOS_DIVERGENCIA);

        return VIEW_ATUALIZAR_ANEXOS_FORM;
    }

    /**
     * Quando há divergência entre o executado e o planejado em uma viagem é
     * adicionado a justificativa da prestação de contas e em seguida é atualizado
     * os anexos da requisição.
     * 
     * @param model
     * @param requisicao_id
     * @param motivo
     * @param atividadesDesenvolvidas
     * @param sessao
     * @return
     */
    @PostMapping("/divergenciaJustificativa")
    public String divergenciaJustificativa(Model model, @RequestParam("requisicao_id") Long requisicao_id,
            @RequestParam(value = "motivo", required = true) String motivo,
            @RequestParam(value = "atividadesDesenvolvidas", required = true) String atividadesDesenvolvidas,
            HttpSession sessao) {

        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);

        sessao.setAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        requisicao.setModificada(true);
        requisicao.setJustificativaPrestacao(motivo);
        requisicao.setDescricaoAtividadesDesenvolvidas(atividadesDesenvolvidas);

        this.requisicaoServiceImpl.atualizar(requisicao);

        return REDIRECT_LINK_PRESTACAO_CONTAS.replace("${requisicao_id}", requisicao.getId().toString());
    }
}
