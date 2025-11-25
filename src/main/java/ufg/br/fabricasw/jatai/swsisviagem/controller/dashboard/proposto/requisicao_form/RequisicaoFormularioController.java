package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import static ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form.RequisicaoFormularioHelper.SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import static ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form.RequisicaoFormularioHelper.SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO;

/**
 * O proposto deve poder cadastrar novas requisições ou editar as existentes,
 * este controller lida com estas operações.
 *
 *
 * @author ronogue
 */
@Controller("proposto")
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class RequisicaoFormularioController {

    private static final String TITULO_CADATRO_FORM_VIEW = "Nova requisição";
    private static final String TITULO_ATUALIZAR_FORM_VIEW = "Editar requisição";
    private static final String VIEW_FORM_REQUISICAO = "app/dashboard/proposto/requisicao/formulario";

    private static final String ATTR_SESSION_TITULO_FORMULARIO = "tituloForm";
    private static final String LINK_REDIRECT_DETALHES = "redirect:/dashboard/proposto/requisicoes/detalhes/";

    private static final String SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO = "requisicaoPossuiVeiculoProprio";

    /**
     * FLAG PARA EXIBIR BOTÕES DE EDIÇÃO DA VIEW
     * app/dashboard/proposto/requisicao/formulario
     */
    private static final String ATTR_IS_MODO_EDITAR = "isModoEditar";

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    @Autowired
    private RequisicaoFormularioHelper requisicaoFormularioHelper;

    @ModelAttribute
    public void addAttibute(Model model) {
        model.addAttribute(ATTR_IS_MODO_EDITAR, true);
    }

    @GetMapping("/requisicoes/nova_requisicao")
    public String formNovaRequisicao(Model model, HttpSession sessao) {

        sessao.setAttribute(ATTR_SESSION_TITULO_FORMULARIO, TITULO_CADATRO_FORM_VIEW);
        sessao.setAttribute(SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO, false);
        sessao.setAttribute(SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO, true);
        sessao.setAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB, false);

        this.requisicaoFormularioHelper.addAtributosModel(model, new Requisicao(), sessao);
        return VIEW_FORM_REQUISICAO;
    }

    @GetMapping("/requisicoes/editar/{requisicaoId}")
public String formAtualizarRequisicao(@PathVariable("requisicaoId") Long id, Model model, HttpSession sessao) {

    Requisicao requisicao = this.requisicaoServiceImpl.findForEdicao(id); // <<<<<<<<<<

    if (requisicaoFormularioHelper.isAdicionarPropostoNaLista(sessao)) {
        requisicaoFormularioHelper.salvarPassageiroSolicitanteNaLista(requisicao);
    }

    sessao.setAttribute(SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO, false);
    sessao.setAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB, true);
    sessao.setAttribute(ATTR_SESSION_TITULO_FORMULARIO, TITULO_ATUALIZAR_FORM_VIEW);

    if (requisicao.possuiVeiculoProprio()) {
        sessao.setAttribute(Constantes.SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO, true);
        sessao.setAttribute(Constantes.SESSION_DOC_VEICULO_PROPRIO, requisicao.getDocumentoVeiculoProprio());
    } else {
        sessao.setAttribute(Constantes.SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO, false);
    }

    this.requisicaoFormularioHelper.addAtributosModel(model, requisicao, sessao);
    return VIEW_FORM_REQUISICAO;
}
    /**
     * Salvar o formulário, ou seja a nova requisição.
     *
     * @param model
     * @param requisicao requisição final
     * @param sessao
     * @return caminho para a view (html)
     */
    @PostMapping("/requisicoes/salvar")
    @Transactional(rollbackFor = {DataAccessException.class, Exception.class})
    public String salvarFormNovaRequisicao(Model model, @Validated Requisicao requisicao, HttpSession sessao) {

        Long id = this.requisicaoFormularioHelper.salvar(requisicao).getId();

        if (requisicaoFormularioHelper.isAdicionarPropostoNaLista(sessao)) {

            requisicaoFormularioHelper.salvarPassageiroSolicitanteNaLista(requisicao);
            sessao.setAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB, true);
        }

        return LINK_REDIRECT_DETALHES + id;
    }
}
