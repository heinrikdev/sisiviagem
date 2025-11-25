package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller para controlar o fluxo de dados das unidades/departamento.
 *
 * @author Ronaldo Nogueira de Sousa
 */
@Controller
@RequestMapping("/dashboard/admin/unidade")
@PreAuthorize("hasAuthority('ADMIN')")
public class UnidadeDepartamentoController {

    /*
     * TITULOS DAS PÁGINAS HTML, SERÃO UTILIZADAS PELO THYMELEAF
     */
    private static final String LISTAR_UNIDADES_TITULO = "Unidades/Departamentos cadastrados";
    private static final String CADASTRAR_UNIDADES_TITULO = "Cadastrar unidade/departamento";
    private static final String ATUALIZAR_UNIDADES_TITULO = "Editar unidade/departamento";

    /*
     * CAMINHO PARA O HTML/VIEW
     */
    private static final String LISTAR_UNIDADES_VIEW = "app/dashboard/admin/unidades_departamento/listar";
    private static final String FORMULARIO_UNIDADE_VIEW = "app/dashboard/admin/unidades_departamento/formulario";

    /*
     * LINKS DE REDIRECIONAMENTO
     */
    private static final String REDIRECT_LISTAR_LINK = "redirect:/dashboard/admin/unidade";

    /*
     * NOMES DE ATRIBUTOS UTILIZADO NAS MODELS
     */
    private static final String ATTR_PAGE_UNIDADES = "pageUnidades";
    private static final String ATTR_UNIDADE = "unidade";

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public String listarUnidades(Model model, Pageable pageable, HttpServletRequest request) {

        Page<UnidadeDepartamento> pageUnidades = this.unidadeService.findAllByPage(pageable);
        Constantes.putPageInformationToViewModel(model, pageUnidades, request);

        model.addAttribute(ATTR_PAGE_UNIDADES, pageUnidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, LISTAR_UNIDADES_TITULO);

        return LISTAR_UNIDADES_VIEW;
    }

    @PostMapping
    public String listarUnidadesPorNome(String nome, Model model, Pageable pageable, HttpServletRequest request) {

        Page<UnidadeDepartamento> pageUnidades = this.unidadeService.findByNome(nome, pageable);
        Constantes.putPageInformationToViewModel(model, pageUnidades, request);

        model.addAttribute(ATTR_PAGE_UNIDADES, pageUnidades);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, LISTAR_UNIDADES_TITULO);

        return LISTAR_UNIDADES_VIEW;
    }

    @PostMapping("/formulario")
    public String salvarFormUnidadeDepartamento(@ModelAttribute("unidade") UnidadeDepartamento unidadeDepartamento) {

        this.unidadeService.save(unidadeDepartamento);
        return REDIRECT_LISTAR_LINK;
    }

    @GetMapping("/cadastrar")
    public String formCadastrarUnidadeDepartamento(Model model) {

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, CADASTRAR_UNIDADES_TITULO);
        model.addAttribute(Constantes.ATTR_UNIDADES_DEPARTAMENTO, Constantes.UNIDADES_DEPARTAMENTO);
        model.addAttribute(ATTR_UNIDADE, new UnidadeDepartamento());

        return FORMULARIO_UNIDADE_VIEW;
    }

    @GetMapping("/editar/{id}")
    public String formEditarUnidadeDepartamento(Model model, @PathVariable("id") Long id) {

        UnidadeDepartamento unidadeDepartamento = this.unidadeService.findOne(id);

        model.addAttribute(Constantes.ATTR_TITLE_PAGE, ATUALIZAR_UNIDADES_TITULO);
        model.addAttribute(Constantes.ATTR_UNIDADES_DEPARTAMENTO, Constantes.UNIDADES_DEPARTAMENTO);
        model.addAttribute(ATTR_UNIDADE, unidadeDepartamento);

        return FORMULARIO_UNIDADE_VIEW;
    }
}
