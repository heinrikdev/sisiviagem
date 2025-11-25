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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.service.AnexoService;
import ufg.br.fabricasw.jatai.swsisviagem.storage.StorageService;

/**
 *
 * @author Ronaldo N Sousa Criado em: 12/02/2019
 */
@Controller
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class ListaAnexoController {

    private static final String VIEW_FORM_REQUISICAO = "app/dashboard/proposto/requisicao/formulario";
    private static final String LINK_REDIRECT_FORM_EDITAR = "redirect:/dashboard/proposto/requisicoes/editar/";
    private static final String ATTR_ERROR_VALIDATION_ANEXO_FORM = "formAnexoValidation";

    @Autowired
    private StorageService fileSystemStorageService;
    
    @Autowired
    private AnexoService anexoService;

    @Autowired
    private RequisicaoFormularioHelper requisicaoFormularioHelper;
    
    /**
     * Link de submit do formulário para adicionar um novo anexo à requisição
     *
     * @param model detentor dos atributos que serão exibidos na view.
     * @param requisicao requisição do formulário que está na view. Isto é usado
     * para manter as informações preenchidas no formulário durante os submites
     * de adição de passageiro, anexo, etc.
     * @param outro_rotulo
     * @param anexo anexo a ser adicionado
     * @param validation validações do formulário para anexo
     * @param ra
     * @param sessao
     * @return caminho para a view (html)
     */
    @PostMapping("/formulario/adicionar_anexo")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String addAnexo(
            Model model,
            Requisicao requisicao,
            @RequestParam(value = "rotulo_outro", required = false) String outro_rotulo,
            @Valid Anexo anexo,
            BindingResult validation,
            RedirectAttributes ra,
            HttpSession sessao) {

        if (!validation.hasErrors()) {

            if (outro_rotulo != null && !outro_rotulo.trim().isEmpty()) {
                anexo.setRotulo(outro_rotulo);
            }

            Long requisicao_id = this.requisicaoFormularioHelper.addicionarAnexo(requisicao, anexo).getId();
            return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#anexo";
            
        } else {

            this.requisicaoFormularioHelper.addAtributosModel(model, requisicao, sessao);
            model.addAttribute(RequisicaoFormularioHelper.ATTR_ANEXO, anexo);
            model.addAttribute(ATTR_ERROR_VALIDATION_ANEXO_FORM, new ValidationWrapper(validation));
        }

        return VIEW_FORM_REQUISICAO;
    }

    /**
     * Modifica os itens da lista de anexos.
     *
     * @param requisicao_id
     * @param id_anexo indice do anexo na lista
     * @param ra
     * @return caminho para a view (html)
     */
    @GetMapping("/requisicoes/remover_anexo/{requisicao_id}/{id_anexo}")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String removerAnexo(@PathVariable("requisicao_id") Long requisicao_id, @PathVariable("id_anexo") Long id_anexo, RedirectAttributes ra) {

        Anexo anexo = this.anexoService.find(id_anexo);
        
        this.fileSystemStorageService.deletar(anexo);
        this.anexoService.deleteAnexo(anexo.getId());
        
        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#anexo";
    }
}
