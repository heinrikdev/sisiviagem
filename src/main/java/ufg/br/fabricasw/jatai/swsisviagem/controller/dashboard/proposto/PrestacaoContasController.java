package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto;

import java.util.List;
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
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.service.AnexoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.storage.StorageService;

/**
 * Controller responsável pelo processamento da prestação de contas ao termino
 * da requisição.
 *
 * @author ronogue
 */
@Controller("prestacao_contas_proposto")
@RequestMapping("dashboard/proposto/requisicao")
@PreAuthorize("hasAuthority('USER')")
public class PrestacaoContasController {

    private static final String TITULO_PRESTACAO_CONTAS_FORM        = "Prestação de Contas";
    private static final String REDIRECT_LINK_REQUISICAO_DETALHES   = "redirect:/dashboard/proposto/requisicoes/detalhes/";
    
    private static final String VIEW_FORMULARIO         = "app/dashboard/proposto/prestacao_contas/formulario";
    private static final String ATTR_ANEXO              = "anexo";
    private static final String ATTR_FORM_VALIDATION    = "formAnexoValidation";
    
    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;
    
    @Autowired
    private AnexoService anexoService;
    
    @Autowired
    private StorageService fileSystemStorageService;
    
    @GetMapping("/{requisicao_id}/prestacao_contas")
    public String index(Model model, @PathVariable("requisicao_id") Long requisicao_id, @RequestParam(name = "atividadeDesenvolvida", required = false) String atividadeDesenvolvida, HttpSession sessao) {
        
        Requisicao requisicao = this.requisicaoServiceImpl.findById(requisicao_id);
        
        if (atividadeDesenvolvida != null) {
            
            requisicao.setDescricaoAtividadesDesenvolvidas(atividadeDesenvolvida);
            this.requisicaoServiceImpl.updateDescricaoAtividadesDesenvolvidas(atividadeDesenvolvida, requisicao_id);
        }
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PRESTACAO_CONTAS_FORM);
        this.initModel(model, sessao, requisicao);
        
        return VIEW_FORMULARIO;
    }
 
    @PostMapping("/prestacao_contas/add_anexo")
    public String addAnexo(Model model, @Valid Anexo anexo, BindingResult validation, HttpSession sessao) {
        
        Boolean hasErrors   = validation.hasFieldErrors("nome") || validation.hasFieldErrors("file");
    
        if (!hasErrors) {
            
            anexo.setRotulo("Prestação de contas");
            this.fileSystemStorageService.store(anexo);
            Anexo anx = this.anexoService.save(anexo);
            
            Requisicao requisicao   = (Requisicao)sessao.getAttribute(Constantes.ATTR_REQUISICAO);
            requisicao.getPrestacaoDeContas().add(anx);
            
            this.requisicaoServiceImpl.addPrestacaoContas(requisicao.getId(), anx.getId());
            return "redirect:/dashboard/proposto/requisicao/" + requisicao.getId() + "/prestacao_contas";
            
        } else {
            
            model.addAttribute(ATTR_ANEXO, anexo);
        }
        
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PRESTACAO_CONTAS_FORM);
        model.addAttribute(ATTR_FORM_VALIDATION, new ValidationWrapper(validation));
        this.updateModel(model, sessao);
        
        return VIEW_FORMULARIO;
    }
    
    /**
     * Atualiza a requisição com os anexos da prestação de contas.
     * 
     * @param model
     * @param sessao
     * @return 
     */
    @PostMapping("/prestacao_contas")
    @Transactional(rollbackFor = {DataAccessException.class, InvalidDataAccessApiUsageException.class, Exception.class})
    public String salvarPrestacaoContas(Model model, HttpSession sessao) {
        
        Requisicao requisicao   = (Requisicao)sessao.getAttribute(Constantes.ATTR_REQUISICAO);
        
        StringBuilder builder = new StringBuilder(REDIRECT_LINK_REQUISICAO_DETALHES);
        builder.append(requisicao.getId().toString());
        
        return builder.toString();
    }
    
    private void initModel(Model model, HttpSession sessao, Requisicao requisicao) {
        
        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(Constantes.ATTR_LISTA_PRESTACAO_CONTAS, requisicao.getPrestacaoDeContas());
        
        sessao.setAttribute(Constantes.ATTR_SESSION_PRESTACAO_CONTAS, requisicao.getPrestacaoDeContas());
        sessao.setAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        
        model.addAttribute(ATTR_ANEXO, new Anexo());
        model.addAttribute(ATTR_FORM_VALIDATION, new ValidationWrapper());
    }
    
    private void updateModel(Model model, HttpSession sessao) {
        
        List<Anexo> anexos      = (List<Anexo>) sessao.getAttribute(Constantes.ATTR_SESSION_PRESTACAO_CONTAS);
        Requisicao requisicao   = (Requisicao)sessao.getAttribute(Constantes.ATTR_REQUISICAO);
        
        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(Constantes.ATTR_LISTA_PRESTACAO_CONTAS, anexos);
    }
}
