package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.transporte.viagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.service.*;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailMotoristaViagemService;
import ufg.br.fabricasw.jatai.swsisviagem.service.email.EmailViagemAgendadaService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * O proponente transporte é responsável por criar viagens que atenderão as
 * requisições que lhe cabe.
 *
 * Uma viagem pode atender 1 ou N requisições.
 *
 * @author ronogue
 */
@Controller("transporte_viagem_requisicao")
@RequestMapping("/dashboard/transporte/viagem")
@PreAuthorize("hasAuthority('TRANSPORTE')")
public class ViagemFormularioController {

    private static final String TITULO_PAGE_NOVA_VIAGEM = "Adicionar nova viagem";
    private static final String TITULO_PAGE_EDITAR_VIAGEM = "Editar viagem ";

    private static final String VIEW_FORMULARIO_NOVA_VIAGEM = "app/dashboard/transporte/viagem/formulario";
    private static final String ATTR_NOVA_VIAGEM = "viagem";

    private static final String ATTR_LISTA_MOTORISTAS_PARA_ADD = "motoristas";
    private static final String ATTR_LISTA_MOTORISTAS_ADICIONADOS = "motoristasAdicionados";

    private static final String ATTR_LISTA_VEICULOS_PARA_ADD = "veiculos";
    private static final String ATTR_LISTA_VEICULOS_ADICIONADOS = "veiculosAdicionados";
    private static final String ATTR_LISTA_REQUISICAOS = "requisicaos";

    private static final String REDIRECT_LINK_VIAGEM_DETALHES = "redirect:/dashboard/transporte/viagem/${viagem_id}/detalhes";

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private MotoristaService motoristaService;

    @Autowired
    private VeiculoService veiculoService;

    @Autowired
    private EmailMotoristaViagemService emailMotoristaViagemService;

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private EmailViagemAgendadaService emailViagemAgendadaService;

    @GetMapping("/nova")
    public String indexFormulario(Model model, HttpSession sessao) {

        this.initModel(model, new Viagem(), sessao, Boolean.TRUE);
        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    @GetMapping("/editar/{viagem_id}")
    public String indexEditFormulario(Model model, @PathVariable("viagem_id") Long viagem_id, HttpSession sessao) {

        Viagem viagem = this.viagemService.find(viagem_id);
        this.initModel(model, viagem, sessao, Boolean.TRUE);
        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    @PostMapping("/nova")
    public String salvarFormulario(Model model, HttpSession sessao, @Valid Viagem viagem, BindingResult validation) {

        List<Motorista> motoristasAdicionados = (List<Motorista>) sessao
                .getAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS);
        List<Veiculo> veiculosAdicionados = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS);
        List<Requisicao> requisicaos = (List<Requisicao>) sessao.getAttribute(ATTR_LISTA_REQUISICAOS);

        if (viagem.getId() != null) {

            Viagem tmp = this.viagemService.find(viagem.getId());

            tmp.setTitulo(viagem.getTitulo());
            tmp.setStartHora(viagem.getStartHora());
            tmp.setEndHora(viagem.getEndHora());
            tmp.setData(viagem.getData());
            tmp.setDataTermino(viagem.getDataTermino());
            tmp.setDescricao(viagem.getDescricao());

            List<Tarefa> tarefas = this.tarefaService.findAll(viagem);
            viagem.getTarefas().addAll(tarefas);

            viagem = tmp;
        }

        viagem.setMotoristas(motoristasAdicionados);
        viagem.setVeiculos(veiculosAdicionados);
        viagem.setRequisicaos(requisicaos);

        Viagem v = this.viagemService.save(viagem);

        StringBuilder builder = new StringBuilder(REDIRECT_LINK_VIAGEM_DETALHES);
        builder.replace(38, 50, v.getId().toString());

        this.emailMotoristaViagemService.avisarMotoristasNovaViagem(viagem);

        for (Requisicao requisicao : requisicaos) {
            this.emailViagemAgendadaService.sendAlertChangeViagem(viagem, requisicao);
        }

        this.clearSession(sessao);

        return builder.toString();
    }

    /**
     * Adiciona veiculo a viagem.
     * 
     * @param model
     * @param sessao
     * @param viagem
     * @param index
     * @return template de formulário da viagem.
     */
    @PostMapping("/adicionar_veiculo/{index_veiculo}")
    public String adicionarVeiculo(Model model, HttpSession sessao, Viagem viagem,
            @PathVariable("index_veiculo") int index) {

        List<Veiculo> veiculos = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_PARA_ADD);
        List<Veiculo> veiculosAdicionados = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS);

        Veiculo veiculo = veiculos.remove(index);
        veiculosAdicionados.add(veiculo);
        this.initModel(model, viagem, sessao, Boolean.FALSE);

        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    /**
     * Remove veiculo da viagem.
     * 
     * @param model
     * @param sessao
     * @param viagem
     * @param index
     * @return template de formulário da viagem.
     */
    @PostMapping("/remover_veiculo/{index_veiculo}")
    public String removerVeiculo(Model model, HttpSession sessao, Viagem viagem,
            @PathVariable("index_veiculo") int index) {

        List<Veiculo> veiculos = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_PARA_ADD);
        List<Veiculo> veiculosAdicionados = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS);

        Veiculo veiculo = veiculosAdicionados.remove(index);
        veiculos.add(veiculo);
        this.initModel(model, viagem, sessao, Boolean.FALSE);

        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    /**
     * Adiciona veiculo a viagem.
     * 
     * @param model
     * @param sessao
     * @param viagem
     * @param index
     * @return template de formulário da viagem.
     */
    @PostMapping("/adicionar_motorista/{index_motorista}")
    public String adicionarMotorista(Model model, HttpSession sessao, Viagem viagem,
            @PathVariable("index_motorista") int index) {

        List<Motorista> motoristas = (List<Motorista>) sessao.getAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD);
        List<Motorista> motoristasAdicionados = (List<Motorista>) sessao
                .getAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS);

        Motorista motorista = motoristas.remove(index);
        motoristasAdicionados.add(motorista);
        this.initModel(model, viagem, sessao, Boolean.FALSE);

        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    /**
     * Remove veiculo da viagem
     * 
     * @param model
     * @param sessao
     * @param viagem
     * @param index  - indice do veiculo no ArrayList veiculosAdicionados
     * @return template de formulário da viagem.
     */
    @PostMapping("/remover_motorista/{index_motorista}")
    public String removerMotorista(Model model, HttpSession sessao, Viagem viagem,
            @PathVariable("index_motorista") int index) {

        List<Motorista> motoristas = (List<Motorista>) sessao.getAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD);
        List<Motorista> motoristasAdicionados = (List<Motorista>) sessao
                .getAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS);

        Motorista motorista = motoristasAdicionados.remove(index);
        motoristas.add(motorista);

        this.initModel(model, viagem, sessao, Boolean.FALSE);

        if (viagem.isViagemCadastrada()) {
            this.emailMotoristaViagemService.avisarRemocao(motorista, viagem);
        }

        return VIEW_FORMULARIO_NOVA_VIAGEM;
    }

    /**
     * Remove os atributos da sessão do usuário.
     * 
     * @param sessao HttpSession
     */
    private void clearSession(HttpSession sessao) {

        sessao.removeAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD);
        sessao.removeAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS);
        sessao.removeAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS);
        sessao.removeAttribute(ATTR_LISTA_VEICULOS_PARA_ADD);
    }

    /**
     * Inicializa o model com alguns atributos padrões.
     * 
     * @param model
     * @param viagem
     * @param sessao
     * @param isSessaoInit true -> inicializa os atributos da sessao (lista de
     *                     motoristas e veiculos),
     *                     false -> atualiza os atributos da sessao.
     */
    private void initModel(Model model, Viagem viagem, HttpSession sessao, Boolean isSessaoInit) {

        List<Motorista> motoristas;
        List<Veiculo> veiculos;

        List<Veiculo> veiculosAdicionados;
        List<Motorista> motoristasAdicionados;

        if (isSessaoInit) {

            if (viagem.getId() != null) {

                motoristas = this.motoristaService.findAllMotoristaExcetoNaViagem(viagem.getId());
                veiculos = this.veiculoService.findAllVeiculoExcetoNaViagem(viagem.getId());

            } else {

                motoristas = this.motoristaService.findAll();
                veiculos = this.veiculoService.findAll();
            }

            veiculosAdicionados = new ArrayList<>();
            motoristasAdicionados = new ArrayList<>();

            sessao.setAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS, motoristasAdicionados);
            sessao.setAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD, motoristas);
            sessao.setAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS, veiculosAdicionados);
            sessao.setAttribute(ATTR_LISTA_VEICULOS_PARA_ADD, veiculos);
            sessao.setAttribute(ATTR_LISTA_REQUISICAOS, viagem.getRequisicaos());

        } else {

            motoristas = (List<Motorista>) sessao.getAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD);
            veiculos = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_PARA_ADD);

            veiculosAdicionados = (List<Veiculo>) sessao.getAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS);
            motoristasAdicionados = (List<Motorista>) sessao.getAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS);

        }

        if (viagem.getId() != null) {

            StringBuilder builder = new StringBuilder(TITULO_PAGE_EDITAR_VIAGEM);
            builder.append(viagem.getId().toString());

            motoristasAdicionados.addAll(viagem.getMotoristas());
            veiculosAdicionados.addAll(viagem.getVeiculos());

            model.addAttribute(Constantes.ATTR_TITLE_PAGE, builder.toString());

        } else {

            model.addAttribute(Constantes.ATTR_TITLE_PAGE, TITULO_PAGE_NOVA_VIAGEM);
        }

        model.addAttribute(ATTR_LISTA_MOTORISTAS_ADICIONADOS, motoristasAdicionados);
        model.addAttribute(ATTR_LISTA_MOTORISTAS_PARA_ADD, motoristas);
        model.addAttribute(ATTR_LISTA_VEICULOS_ADICIONADOS, veiculosAdicionados);
        model.addAttribute(ATTR_LISTA_VEICULOS_PARA_ADD, veiculos);
        model.addAttribute(ATTR_NOVA_VIAGEM, viagem);
    }
}
