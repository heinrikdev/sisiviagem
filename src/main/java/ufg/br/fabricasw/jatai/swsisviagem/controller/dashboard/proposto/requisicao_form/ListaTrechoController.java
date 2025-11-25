package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.TrechoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoProprioService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ronaldo N Sousa Criado em: 12/02/2019
 */
@Controller
@RequestMapping("/dashboard/proposto")
@PreAuthorize("hasAuthority('USER')")
public class ListaTrechoController {

    private static final String LINK_REDIRECT_FORM_EDITAR = "redirect:/dashboard/proposto/requisicoes/editar/";
    private static final String VIEW_FORM_REQUISICAO = "app/dashboard/proposto/requisicao/formulario";
    private static final String ATTR_ERROR_VALIDATION_TRECHO_FORM = "formTrechoValidation";
    private static final String ATTR_TRECHO = "trecho";

    @Autowired
    private RequisicaoFormularioHelper requisicaoHelper;

    @Autowired
    private VeiculoProprioService veiculoProprioService;

    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    @Autowired
    private TrechoServiceImpl trechoService;

    /**
     * Link de submit do formulário para adicionar um novo anexo à requisição
     *
     * @param model
     * @param requisicao requisição do formulário que está na view. Isto é usado
     *                   para manter as informações preenchidas no formulário
     *                   durante os submites
     *                   de adição de passageiro, anexo, etc.
     * @param trecho     trecho a ser adicionado
     * @param validation validações do formulário para anexo
     * @param sessao
     * @return caminho para a view (html)
     */
    @PostMapping("/formulario/adicionar_trecho")
    @Transactional(rollbackFor = { DataAccessException.class, InvalidDataAccessApiUsageException.class,
            Exception.class })
    public String addTrecho(Model model, @Valid Requisicao requisicao, @Valid Trecho trecho, BindingResult validation,
            HttpSession sessao, RedirectAttributes redirectAttributes) {

        boolean tipoTrechoValido = (trecho.isTransporteVeiculoProprio()
                && requisicaoHelper.possuiDocumentoVeiculoNaSessao(sessao)) || !(trecho.isTransporteVeiculoProprio());

        if (!validation.hasErrors() && tipoTrechoValido) {

            Long requisicao_id;

            if (trecho.getId() != null) {

                this.trechoService.atualizar(trecho);

                if (trecho.isTransporteVeiculoProprio() && requisicaoHelper.possuiDocumentoVeiculoNaSessao(sessao)) {

                    VeiculoProprio docVeiculoProprio = requisicaoHelper.fetchDocumentoVeiculoProprio(sessao);

                    requisicao.setDocumentoVeiculoProprio(docVeiculoProprio);
                    docVeiculoProprio.setRequisicao(requisicao);
                }

                /**
                 * Se você tem amor a vida, não mexe nessas duas linhas, ou melhor, não mexe
                 * nesse bloco
                 * só se for realmente necessário. Ser for realmente necessário, que os deuses
                 * tenham piedade
                 * de vossa alma ^^.
                 */
                requisicao = this.requisicaoHelper.salvar(requisicao);
                requisicao_id = this.requisicaoHelper.adcionarTrecho(requisicao).getId();

                return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#trecho";
            }

            if ("Ida e Volta".equals(trecho.getTipoTrecho())) {

                Trecho trechoVolta = trecho.clone();

                trechoVolta.setCidadeDestino(trecho.getCidadeOrigem());
                trechoVolta.setCidadeOrigem(trecho.getCidadeDestino());

                if (!trecho.getIsDestinoDoBrasil()) {
                    trechoVolta.setIsOrigemDoBrasil(false);
                }

                if (!trecho.getIsOrigemDoBrasil()) {
                    trechoVolta.setIsDestinoDoBrasil(false);
                }

                trechoVolta.setEstadoDestino(trecho.getEstadoOrigem());
                trechoVolta.setEstadoOrigem(trecho.getEstadoDestino());

                trechoVolta.setDataChegada(trecho.getDataSaida());
                trechoVolta.setDataSaida(trecho.getDataChegada());

                trecho.setTipoTrecho("Ida");
                trechoVolta.setTipoTrecho("Volta");

                List<Trecho> trechos = new ArrayList<>(2);

                trechos.add(this.trechoService.save(trecho));
                trechos.add(this.trechoService.save(trechoVolta));

                if (trecho.isTransporteVeiculoProprio() && requisicaoHelper.possuiDocumentoVeiculoNaSessao(sessao)) {

                    VeiculoProprio docVeiculoProprio = requisicaoHelper.fetchDocumentoVeiculoProprio(sessao);

                    requisicao.setDocumentoVeiculoProprio(docVeiculoProprio);
                    docVeiculoProprio.setRequisicao(requisicao);
                }

                requisicao_id = this.requisicaoHelper.adcionarTrecho(requisicao, trechos).getId();

            } else {

                this.trechoService.save(trecho);

                if (trecho.isTransporteVeiculoProprio() && requisicaoHelper.possuiDocumentoVeiculoNaSessao(sessao)) {

                    VeiculoProprio docVeiculoProprio = requisicaoHelper.fetchDocumentoVeiculoProprio(sessao);

                    requisicao.setDocumentoVeiculoProprio(docVeiculoProprio);
                    docVeiculoProprio.setRequisicao(requisicao);
                }

                requisicao_id = this.requisicaoHelper.adcionarTrecho(requisicao, trecho).getId();
            }

            return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#trecho";
        } else {

            ValidationWrapper validationWrapper = new ValidationWrapper(validation);

            if (!tipoTrechoValido) {

                BindingResult bindResult = validationWrapper.getValidation();
                bindResult.rejectValue("tipoTransporte", "tipoTransporte",
                        "O formulário do veículo próprio deve ser preenchido!");
            }

            this.requisicaoHelper.addAtributosModel(model, requisicao, sessao);

            model.addAttribute(ATTR_TRECHO, trecho);
            model.addAttribute(ATTR_ERROR_VALIDATION_TRECHO_FORM, validationWrapper);

            return VIEW_FORM_REQUISICAO;
        }
    }

    @GetMapping("/formulario/remover_trecho/{trecho_id}/{requisicao_id}")
    @Transactional(rollbackFor = { DataAccessException.class, InvalidDataAccessApiUsageException.class,
            Exception.class })
    public String remover(@PathVariable("trecho_id") Long trecho_id,
            @PathVariable("requisicao_id") Long requisicao_id) {

        this.trechoService.apagar(trecho_id);

        Requisicao requisicao = this.requisicaoService.find(requisicao_id);
        boolean docRemovido = requisicao.verificarNecessidadeRemovacaoDocVeiculoProprio(this.veiculoProprioService);

        if (docRemovido) {

            this.requisicaoService.save(requisicao);
        }

        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#trecho";
    }

    @GetMapping("/formulario/editar_trecho/{trecho_id}/{requisicao_id}")
    public String editar(@PathVariable("trecho_id") Long trecho_id, @PathVariable("requisicao_id") Long requisicao_id,
            final RedirectAttributes redirectAttrs) {

        Trecho trecho = this.trechoService.find(trecho_id);
        redirectAttrs.addFlashAttribute(ATTR_TRECHO, trecho);

        return LINK_REDIRECT_FORM_EDITAR + requisicao_id + "?#trecho";
    }
}
