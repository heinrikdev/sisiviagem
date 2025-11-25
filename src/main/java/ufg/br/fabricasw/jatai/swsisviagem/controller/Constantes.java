package ufg.br.fabricasw.jatai.swsisviagem.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EUnidadeDepartamento;

/**
 *
 * @author Ronaldo N. de Sousa
 */
public class Constantes {

    public static final String ATTR_TITLE_PAGE = "titlePage";
    public static final String ATTR_UNIDADES_DEPARTAMENTO = "unidadesDepartamento";
    public static final String ATTR_PAGE_NEXT = "proximaPagina";
    public static final String ATTR_PAGE_PREV = "paginaAnterior";
    public static final String ATTR_UNIDADES = "unidades";
    public static final String ATTR_ESTADOS = "estados";
    public static final String ATTR_MUNICIPIOS = "municipios";
    public static final String ATTR_REQUISICAO = "requisicao";
    public static final String ATTR_LISTA_PRESTACAO_CONTAS = "listPrestacaoContas";

    /**
     * VARIÁVEIS DE SESSÃO
     */
    public static final String ATTR_SESSION_PASSAGEIROS = "passageiros";
    public static final String ATTR_SESSION_TRECHOS_IDA = "trechos_ida";
    public static final String ATTR_SESSION_TRECHOS_VOLTA = "trechos_volta";
    public static final String ATTR_SESSION_ANEXOS = "anexos";
    public static final String ATTR_SESSION_PRESTACAO_CONTAS = "prestacaoContas";

    public static final String SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO = "requisicaoPossuiVeiculoProprio";
    public static final String SESSION_DOC_VEICULO_PROPRIO = "documentoVeiculoProprio";

    public static final String ATTR_TIPO_REQUISICAO_ADM = "unidadesAdministrativa";
    public static final String ATTR_TIPO_REQUISICAO_GRA = "unidadesGraduacao";

    public static final String LINK_REDIRECT_TO_DASHBOARD = "redirect:/dashboard";

    /*
     * LISTA DE ENUMS PARA O TIPO DA UNIDADE
     */
    public static final EUnidadeDepartamento[] UNIDADES_DEPARTAMENTO = {
            EUnidadeDepartamento.UnidadeAcademica,
            EUnidadeDepartamento.UnidadeAdministrativa
    };

    /**
     * Adciona na model os atributos de página (link para página anterior e
     * seguinte).
     *
     * Atributos names inseridos: ${proximaPagina}, ${paginaAnterior}
     *
     * @param model   - Model da view
     * @param page    - a página
     * @param request - HttpServletRequest, utilizada para compor o link da
     *                página.
     */
    public static void putPageInformationToViewModel(Model model, Page page, HttpServletRequest request) {

        UriComponentsBuilder nextPage = ServletUriComponentsBuilder.fromServletMapping(request)
                .path(request.getRequestURI());

        request.getParameterMap().forEach((key, value1) -> {
            for (String value : value1) {
                nextPage.queryParam(key, value);
            }
        });

        UriComponentsBuilder prevPage = nextPage.cloneBuilder();

        Pageable pageable = page.getPageable();

        Integer nextPageNumber = (pageable.getPageNumber() + 1);
        Integer prevPageNumber = (pageable.getPageNumber() - 1);

        nextPage.replaceQueryParam("size", pageable.getPageSize());
        prevPage.replaceQueryParam("size", pageable.getPageSize());

        nextPage.replaceQueryParam("page", nextPageNumber);
        prevPage.replaceQueryParam("page", prevPageNumber);

        String nextPageQueryLink = nextPage.buildAndExpand().toUriString();
        String prevPageQueryLink = prevPage.buildAndExpand().toUriString();

        model.addAttribute(ATTR_PAGE_NEXT, nextPageQueryLink);
        model.addAttribute(ATTR_PAGE_PREV, prevPageQueryLink);
    }
}
