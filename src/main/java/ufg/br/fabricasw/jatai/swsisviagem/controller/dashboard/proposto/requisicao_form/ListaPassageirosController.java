package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form.RequisicaoFormularioHelper.SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoPassageiroService;
import static ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form.RequisicaoFormularioHelper.SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO;

/**
 *
 * @author Ronaldo N Sousa Criado em: 12/02/2019
 */
@Controller
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class ListaPassageirosController {

    private static final String VIEW_FORM_REQUISICAO = "app/dashboard/proposto/requisicao/formulario";

    private static final String MOSTRAR_MODAL_PASSAGEIRO = "show_modal_passageiro";
    private static final String ATTR_PASSAGEIRO = "requisicaoPassageiro";
    private static final String LINK_REDIRECT_FORM_EDITAR = "redirect:/dashboard/proposto/requisicoes/editar/";

    private static final String ATTR_ERROR_VALIDATION_PASSAGEIRO_FORM = "formPassageiroValidation";

    @Autowired
    private RequisicaoPassageiroService requisicaoPassageiroService;
    
    @Autowired
    private RequisicaoFormularioHelper requisicaoFormularioHelper;

    /**
     * Modifica os itens da lista de passageiros.
     *
     * @param requisicao
     * @param passageiro_id
     * @param sessao
     * @return caminho para a view (html)
     */
    @PostMapping("/requisicoes/passageiro/remover/{passageiro_id}")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String removerPassageiro(Requisicao requisicao, @PathVariable("passageiro_id") Long passageiro_id, HttpSession sessao) {

        Long requisicao_id = this.requisicaoFormularioHelper.salvar(requisicao).getId();
        
        if (passageiro_id.equals(RequisicaoFormularioHelper.PASSAGEIRO_PROPOSTO_AINDA_NAO_SALVO_ID)) {

            sessao.setAttribute(SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO, false);

        } else {

            this.requisicaoPassageiroService.removerPassageiroDaRequisicao(passageiro_id);
        }

        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#passageiro";
    }

    /**
     * Modifica os itens da lista de passageiros.
     *
     * @param requisicao
     * @param passageiro_id
     * @param sessao
     * @param redirectAttrs
     * @return caminho para a view (html)
     */
    @PostMapping("/requisicoes/passageiro/editar/{passageiro_id}")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String editarPassageiro(Requisicao requisicao, @PathVariable("passageiro_id") Long passageiro_id, HttpSession sessao, final RedirectAttributes redirectAttrs) {

        RequisicaoPassageiro requisicaoPassageiro;
        
        Long requisicao_id = this.requisicaoFormularioHelper.salvar(requisicao).getId();

        if (passageiro_id.equals(RequisicaoFormularioHelper.PASSAGEIRO_PROPOSTO_AINDA_NAO_SALVO_ID)) {

            requisicaoPassageiro = requisicaoFormularioHelper.salvarPassageiroSolicitante(requisicao_id);
            sessao.setAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB, true);

        } else {

            requisicaoPassageiro = this.requisicaoPassageiroService.find(passageiro_id);
        }

        redirectAttrs.addFlashAttribute(MOSTRAR_MODAL_PASSAGEIRO, true);
        redirectAttrs.addFlashAttribute(ATTR_PASSAGEIRO, requisicaoPassageiro);
        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#passageiro.editar";
    }

    /**
     * Link de submit do formulário para adicionar um novo anexo à requisição
     *
     * @param model detentor dos atributos que serão exibidos na view.
     * @param requisicao requisição do formulário que está na view. Isto é usado
     * para manter as informações preenchidas no formulário durante os submites
     * de adição de passageiro, anexo, etc.
     * @param passageiroRequisicao passageiro a ser adicionado
     * @param validation validações do formulário para anexo
     * @param sessao sessão do usuário atual no sistema, lugar onde é armazenado
     * as listas temporariamente.
     * @return caminho para a view (html)
     */
    @PostMapping("/formulario/adicionar_passageiro")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String addPassageiros(Model model, @Valid Requisicao requisicao, @Valid RequisicaoPassageiro passageiroRequisicao, BindingResult validation, HttpSession sessao) {

        if (!validation.hasErrors()) {

            if ( passageiroRequisicao.getId() != null && passageiroRequisicao.getId().equals(RequisicaoFormularioHelper.PASSAGEIRO_PROPOSTO_AINDA_NAO_SALVO_ID)) {
                
                passageiroRequisicao.setId(null);
                sessao.setAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB, true);
                sessao.setAttribute(SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO, false);
            }

            Long requisicao_id = this.requisicaoFormularioHelper.adicionarPassageiro(requisicao, passageiroRequisicao).getId();
            return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#passageiro";

        } else {

            this.requisicaoFormularioHelper.addAtributosModel(model, requisicao, sessao);
            
            model.addAttribute(MOSTRAR_MODAL_PASSAGEIRO, true);
            model.addAttribute(ATTR_PASSAGEIRO, passageiroRequisicao);
            model.addAttribute(ATTR_ERROR_VALIDATION_PASSAGEIRO_FORM, new ValidationWrapper(validation));
        }

        return VIEW_FORM_REQUISICAO + "?#passageiro.editar";
    }
}
