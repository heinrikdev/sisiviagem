package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.proposto.requisicao_form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import ufg.br.fabricasw.jatai.swsisviagem.controller.Constantes;
import ufg.br.fabricasw.jatai.swsisviagem.controller.ValidacaoDocumentoController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.UnidadeDepartamento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EInfoTrajetoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoRequisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Estado;
import ufg.br.fabricasw.jatai.swsisviagem.domain.estados_municipios.Municipio;
import ufg.br.fabricasw.jatai.swsisviagem.domain.facade.SolicitacaoFacade;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.ValidationWrapper;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.service.AnexoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.PassageiroService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoPassageiroService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.UnidadeService;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoProprioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.estados_municipios.EstadoMunicipioService;
import ufg.br.fabricasw.jatai.swsisviagem.storage.StorageService;

/**
 *
 * @author Ronaldo N Sousa Criado em: 12/02/2019
 */
@Component
public class RequisicaoFormularioHelper {

    private static final String ATTR_PASSAGEIRO = "requisicaoPassageiro";
    private static final String ATTR_TRECHO = "trecho";
    public static final String ATTR_ANEXO = "anexo";
    private static final String ATTR_TIPOS_TRANSPORTE = "transportes";
    private static final String ATTR_NAME_TIPO_REQUISICAO = "tiposRequisicao";
    private static final String ATTR_REQUISICAO_PASSAGEIROS = "requisicaoPassageiros";

    private static final String ATTR_ERROR_VALIDATION_ANEXO_FORM = "formAnexoValidation";
    private static final String ATTR_ERROR_VALIDATION_TRECHO_FORM = "formTrechoValidation";
    private static final String ATTR_ERROR_VALIDATION_PASSAGEIRO_FORM = "formPassageiroValidation";

    private static final String ATTR_MUNICIPIOS_DESTINO = "municipiosDestino";
    private static final String ATTR_MUNICIPIOS_ORIGEM = "municipiosOrigem";

    private static final String ATTR_SESSION_TITULO_FORMULARIO = "tituloForm";
    private static final String SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO = "requisicaoPossuiVeiculoProprio";

    public static final String SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO = "PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO";
    public static final String SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB = "PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO";

    private static final ETipoRequisicao[] LISTA_TIPOS_REQUISICAO = {
            ETipoRequisicao.Administrativa,
            ETipoRequisicao.Graduacao,
            ETipoRequisicao.Pos_Graduacao
    };

    public static Long PASSAGEIRO_PROPOSTO_AINDA_NAO_SALVO_ID = -1L;

    @Autowired
    private SolicitacaoFacade solicitacaoFacade;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstadoMunicipioService estadoMunicipioService;

    @Autowired
    private StorageService fileSystemStorageService;

    @Autowired
    private AnexoService anexoService;

    @Autowired
    private RequisicaoPassageiroService requisicaoPassageiroService;

    @Autowired
    private PassageiroService passageiroService;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    @Autowired
    private VeiculoProprioService veiculoProprioService;

    public Requisicao salvar(Requisicao requisicao) {

        if (requisicao.getId() != null) {
            return this.requisicaoService.save(this.atualizarCampos(requisicao));
        }

        this.inserirPropostoDaRequisicao(requisicao);
        return this.requisicaoService.save(requisicao);
    }

    public Requisicao adicionarPassageiro(Requisicao dados, RequisicaoPassageiro requisicaoPassageiro) {

        Requisicao requisicao = this.atualizarCampos(dados);
        Passageiro save = this.passageiroService.save(requisicaoPassageiro.getPassageiro());

        requisicaoPassageiro.setRequisicao(requisicao);
        requisicaoPassageiro.setPassageiro(save);

        if (requisicao.getRequisicaoPassageiros().contains(requisicaoPassageiro)) {

            int index = requisicao.getRequisicaoPassageiros().indexOf(requisicaoPassageiro);
            requisicao.getRequisicaoPassageiros().set(index, requisicaoPassageiro);

        } else {

            requisicao.getRequisicaoPassageiros().add(requisicaoPassageiro);
        }

        requisicao = this.requisicaoService.save(requisicao);

        return requisicao;
    }

    @Transactional(rollbackFor = { DataAccessException.class, InvalidDataAccessApiUsageException.class,
            Exception.class })
    public Requisicao addicionarAnexo(Requisicao dados, Anexo anexo) {

        this.fileSystemStorageService.store(anexo);
        Anexo anx = this.anexoService.save(anexo);

        Requisicao requisicao = this.atualizarCampos(dados);

        requisicao.getAnexos().add(anx);
        requisicao = this.requisicaoService.save(requisicao);

        return requisicao;
    }

    public Requisicao adcionarTrecho(Requisicao dados, List<Trecho> trechos) {

        Requisicao requisicao = this.atualizarCampos(dados);

        this.solicitacaoFacade.addTrechos(requisicao, trechos);

        return this.requisicaoService.save(requisicao);
    }

    public boolean possuiDocumentoVeiculoNaSessao(HttpSession sessao) {
        return (boolean) sessao.getAttribute(Constantes.SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO);
    }

    public VeiculoProprio fetchDocumentoVeiculoProprio(HttpSession sessao) {
        return (VeiculoProprio) sessao.getAttribute(Constantes.SESSION_DOC_VEICULO_PROPRIO);
    }

    public Requisicao adcionarTrecho(Requisicao dados, Trecho trecho) {

        Requisicao requisicao = this.atualizarCampos(dados);

        List<Trecho> trechos = new ArrayList<>(1);
        trechos.add(trecho);

        this.solicitacaoFacade.addTrechos(requisicao, trechos);

        return this.requisicaoService.save(requisicao);
    }

    public Requisicao adcionarTrecho(Requisicao dados) {

        Requisicao requisicao = this.atualizarCampos(dados);

        List<Trecho> trechos = requisicao.allTrechos();

        this.solicitacaoFacade.addTrechos(requisicao, trechos);

        return this.requisicaoService.save(requisicao);
    }

    private Requisicao atualizarCampos(Requisicao entidade) {

        if (entidade.getId() != null) {

            Requisicao requisicao = this.requisicaoService.find(entidade.getId());

            requisicao.setTipoRequisicao(entidade.getTipoRequisicao());
            requisicao.setUnidade(entidade.getUnidade());
            requisicao.setDataRequisicao(entidade.getDataRequisicao());
            requisicao.setSolicitacoes(entidade.getSolicitacoes());

            if (entidade.possuiVeiculoProprio()) {

                VeiculoProprio veiculoProprio = this.veiculoProprioService.save(entidade.getDocumentoVeiculoProprio());
                requisicao.setDocumentoVeiculoProprio(veiculoProprio);

                if (!veiculoProprio.hasTermoResponsabilidade()) {

                    Long codigoVerificacao = Long.parseLong(requisicao.getId().toString()
                            + veiculoProprio.getId().toString() + (long) (Math.random() * 94));

                    TermoOpcaoCompromissoResponsabilidade term = new TermoOpcaoCompromissoResponsabilidade();
                    term.setVeiculoProprio(veiculoProprio);
                    term.setCodigoVerificacao(codigoVerificacao);
                    term.setDataEmissao(LocalDate.now());
                    term.setIdentificador(ValidacaoDocumentoController.DOC_TERMO_COMPROMISSO_RESPONSABILIDADE);

                    veiculoProprio.setTermoResponsabilidade(term);

                    entidade.setDocumentoVeiculoProprio(this.veiculoProprioService.save(veiculoProprio));
                }
            }

            requisicao.setJustificativa((entidade.getJustificativa()));
            requisicao.setTitulo((entidade.getTitulo()));

            return requisicao;
        }

        this.inserirPropostoDaRequisicao(entidade);
        return entidade;
    }

    public void addAtributosModel(Model model, Requisicao requisicao, HttpSession sessao) {

        final String tituloFormulario = (String) sessao.getAttribute(ATTR_SESSION_TITULO_FORMULARIO);
        List<Estado> estados = this.estadoMunicipioService.findAllEstados();

        List<Trecho> trechosDeIda = requisicao.getTrechosDeIda();
        List<Trecho> trechosDeVolta = requisicao.getTrechosDeVolta();

        if (this.isAdicionarPropostoNaLista(sessao)) {
            this.addPassageiroSolicitante(requisicao);
        }

        List<RequisicaoPassageiro> requisicaoPassageiros = requisicao.getRequisicaoPassageiros();

        if (model.containsAttribute(ATTR_TRECHO)) {

            Trecho trecho = (Trecho) model.asMap().get(ATTR_TRECHO);

            if (trecho.isTrechoIda()) {

                trechosDeIda.remove(trecho);

            } else {

                trechosDeVolta.remove(trecho);
            }

            List<Municipio> municipiosDestino = this.estadoMunicipioService
                    .findAllMunicipiosByUf(trecho.getEstadoDestino());
            List<Municipio> municipioOrigem = this.estadoMunicipioService
                    .findAllMunicipiosByUf(trecho.getEstadoOrigem());

            model.addAttribute(ATTR_MUNICIPIOS_DESTINO, municipiosDestino);
            model.addAttribute(ATTR_MUNICIPIOS_ORIGEM, municipioOrigem);

        } else {

            List<Municipio> municipios = this.estadoMunicipioService.findAllMunicipiosByUf("GO");

            model.addAttribute(ATTR_MUNICIPIOS_DESTINO, municipios);
            model.addAttribute(ATTR_MUNICIPIOS_ORIGEM, municipios);
            model.addAttribute(ATTR_TRECHO, new Trecho());
        }

        if (model.containsAttribute(ATTR_PASSAGEIRO)) {

            RequisicaoPassageiro requisicaoPassageiro = (RequisicaoPassageiro) model.asMap().get(ATTR_PASSAGEIRO);
            requisicaoPassageiros.remove(requisicaoPassageiro);

        } else {

            model.addAttribute(ATTR_PASSAGEIRO, new RequisicaoPassageiro());
        }

        model.addAttribute(Constantes.ATTR_SESSION_ANEXOS, requisicao.getAnexos());
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_IDA, trechosDeIda);
        model.addAttribute(Constantes.ATTR_SESSION_TRECHOS_VOLTA, trechosDeVolta);

        model.addAttribute(ATTR_ANEXO, new Anexo());
        model.addAttribute(ATTR_TIPOS_TRANSPORTE, ETipoTransporte.values());

        model.addAttribute(Constantes.ATTR_ESTADOS, estados);

        if (requisicao.getTipoRequisicao() != null) {

            List<UnidadeDepartamento> unidades = this.unidadeService
                    .findByTipoRequisicao(requisicao.getTipoRequisicao());
            model.addAttribute(Constantes.ATTR_UNIDADES, unidades);

        } else {

            model.addAttribute(Constantes.ATTR_UNIDADES, new ArrayList<>());
        }

        model.addAttribute(Constantes.ATTR_REQUISICAO, requisicao);
        model.addAttribute(ATTR_REQUISICAO_PASSAGEIROS, requisicaoPassageiros);
        model.addAttribute(ATTR_NAME_TIPO_REQUISICAO, LISTA_TIPOS_REQUISICAO);
        model.addAttribute(Constantes.ATTR_TITLE_PAGE, tituloFormulario);

        model.addAttribute(SESSION_REQUISICAO_POSSUI_VEICULO_PROPRIO, requisicao.possuiVeiculoProprio());

        if (requisicao.possuiVeiculoProprio()) {

            model.addAttribute("veiculoProprio", requisicao.getDocumentoVeiculoProprio());

        } else {

            model.addAttribute("veiculoProprio", new VeiculoProprio());
        }

        this.putDefaultValidations(model);
    }

    private void addPassageiroSolicitante(Requisicao requisicao) {

        Pessoa pessoa = this.usuarioService.findUserLogado().getPessoa();
        RequisicaoPassageiro p = new RequisicaoPassageiro();

        p.setId(PASSAGEIRO_PROPOSTO_AINDA_NAO_SALVO_ID);
        p.setPassageiro(pessoa.getInfomacaoComoPassageiro());
        p.setInfoTrajetoPassageiro(EInfoTrajetoPassageiro.IdaEVolta);
        requisicao.getRequisicaoPassageiros().add(p);
    }

    public boolean isAdicionarPropostoNaLista(HttpSession sessao) {

        Boolean propostoNaListaPassageiro = (Boolean) sessao.getAttribute(SESSION_ADD_PROPOSTO_NA_LISTA_PASSAGEIRO);
        Boolean propostoNaListaPassageiroSalvoNoDB = (Boolean) sessao
                .getAttribute(SESSION_PROPOSTO_ESTA_NA_LISTA_PASSAGEIRO_SALVO_DB);

        if (propostoNaListaPassageiro == null || propostoNaListaPassageiroSalvoNoDB == null)
            return false;

        return propostoNaListaPassageiro && (!propostoNaListaPassageiroSalvoNoDB);
    }

    public void salvarPassageiroSolicitanteNaLista(Requisicao requisicao) {

        requisicao = requisicaoService.save(this.atualizarCampos(requisicao));

        Pessoa pessoa = this.usuarioService.findUserLogado().getPessoa();
        RequisicaoPassageiro requisicaoPassageiro = new RequisicaoPassageiro();

        requisicaoPassageiro.setPassageiro(pessoa.getInfomacaoComoPassageiro());
        requisicaoPassageiro.setInfoTrajetoPassageiro(EInfoTrajetoPassageiro.IdaEVolta);
        requisicaoPassageiro.setRequisicao(requisicao);

        requisicao.getRequisicaoPassageiros().add(requisicaoPassageiro);

        this.requisicaoService.save(this.atualizarCampos(requisicao));
    }

    public RequisicaoPassageiro salvarPassageiroSolicitante(Long requisicao_id) {

        Requisicao requisicao = this.requisicaoService.find(requisicao_id);

        Pessoa pessoa = this.usuarioService.findUserLogado().getPessoa();
        RequisicaoPassageiro requisicaoPassageiro = new RequisicaoPassageiro();

        requisicaoPassageiro.setPassageiro(pessoa.getInfomacaoComoPassageiro());
        requisicaoPassageiro.setInfoTrajetoPassageiro(EInfoTrajetoPassageiro.IdaEVolta);
        requisicaoPassageiro.setRequisicao(requisicao);

        requisicao.getRequisicaoPassageiros().add(requisicaoPassageiro);
        return requisicaoPassageiroService.salvar(requisicaoPassageiro);
    }

    /**
     * Coloca as validações padrão, neste caso nenhuma pois a validação é feita
     * quando é feito post no formulário.
     *
     * @param model
     */
    private void putDefaultValidations(Model model) {

        model.addAttribute(ATTR_ERROR_VALIDATION_ANEXO_FORM, new ValidationWrapper());
        model.addAttribute(ATTR_ERROR_VALIDATION_TRECHO_FORM, new ValidationWrapper());
        model.addAttribute(ATTR_ERROR_VALIDATION_PASSAGEIRO_FORM, new ValidationWrapper());
    }

    private void inserirPropostoDaRequisicao(Requisicao entidade) {

        if (entidade.getProposto() == null) {
            Pessoa pessoa = this.usuarioService.findUserLogado().getPessoa();

            entidade.setProposto(pessoa);
            entidade.setProponente(pessoa.getNome());
        }
    }
}
