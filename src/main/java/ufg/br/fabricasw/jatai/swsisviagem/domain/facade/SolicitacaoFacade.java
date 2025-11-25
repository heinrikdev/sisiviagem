package ufg.br.fabricasw.jatai.swsisviagem.domain.facade;

import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoDiaria;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoPassagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.EEstadoSolicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoDiariaServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoPassagemServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoTransporteServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.TrechoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.VeiculoProprioService;

/**
 * Created by thevilela on 01/02/17.
 */
@Component
public class SolicitacaoFacade {

    @Autowired
    private TrechoServiceImpl trechoService;
    
    @Autowired
    private VeiculoProprioService veiculoProprioService;

    @Autowired
    private SolicitacaoDiariaServiceImpl solicitacaoDiariaService;

    @Autowired
    private SolicitacaoPassagemServiceImpl solicitacaoPassagemService;

    @Autowired
    private SolicitacaoTransporteServiceImpl solicitacaoTransporteService;

    private static Logger log = LoggerFactory.getLogger(SolicitacaoFacade.class.getName());

    /**
     * Metodo que adiciona trechos nas solicitaçoes. TODO Colocar as regras aqui
     *
     * @param requisicao
     * @param trecho
     */
    public void addTrecho(Requisicao requisicao, Trecho trecho) {
        log.trace("Chegando trecho para adicionar");
        log.trace("Trecho {}", trecho);

        //Salvando trecho
        //Pegando o tipo de trasnporte que e deste trecho
        String tt = trecho.getTipoTransporte();

        //Percorrendo Cada Solicitaçao da Requisicao para saber onde jogar esse trecho
        log.trace("Percorrendo solicitaçoes: {}", tt);
        for (Solicitacao solicitacao : requisicao.getSolicitacoes()) {

            if (solicitacao instanceof SolicitacaoDiaria) {//Se for diaria

                if (tt.equals(ETipoTransporte.ONIBUS.toString()) || tt.equals(ETipoTransporte.VEICULO_PROPRIO.toString()) || tt.equals(ETipoTransporte.VEICULO_OFICIAL.toString()) || tt.equals(ETipoTransporte.AEREO.toString())) {
                    //TODO Adiciona trecho a esta Solicitacao

                    log.trace("entrou aqui 1:");

                    trechoService.save(trecho);

                    //TODO Delegar metodo add do List<Trechos> para a classe para usar solicitacao.addTrecho(trecho)
                    solicitacao.addTrecho(trecho);
                    solicitacaoDiariaService.atualizar((SolicitacaoDiaria) solicitacao);
                    log.trace("entrou aqui 2:");

                }

            } else if (solicitacao instanceof SolicitacaoTransporte) {//Se for Transporte

                if (tt.equals(ETipoTransporte.ONIBUS.toString()) || tt.equals(ETipoTransporte.VEICULO_PROPRIO.toString()) || tt.equals(ETipoTransporte.VEICULO_OFICIAL.toString()) || tt.equals(ETipoTransporte.AEREO.toString())) {
                    //TODO Adiciona trecho a esta Solicitacao
                    log.trace("entrou aqui 3:");

                    trechoService.save(trecho);

                    //TODO Delegar metodo add do List<Trechos> para a classe para usar solicitacao.addTrecho(trecho)
                    solicitacao.addTrecho(trecho);
                    solicitacaoTransporteService.atualizar((SolicitacaoTransporte) solicitacao);
                    log.trace("entrou aqui 4:");

                }

            } else if (solicitacao instanceof SolicitacaoPassagem) {//Se tor Passagem

                if (tt.equals(ETipoTransporte.AEREO.toString())) {
                    log.trace("Tipo do Transporte aereo. Adiciona trecho em Passagem:");

                    trechoService.save(trecho);

                    //TODO Delegar metodo add do List<Trechos> para a classe para usar solicitacao.addTrecho(trecho)
                    solicitacao.addTrecho(trecho);
                    solicitacaoPassagemService.atualizar((SolicitacaoPassagem) solicitacao);
                }

            } else {
                throw new IllegalArgumentException();
            }

            break;

        }
    }

    /**
     * Metodo que adiciona trechos nas solicitaçoes e adiciona ao documento do
     * veiculo prórprio caso haja um.
     *
     * @param requisicao
     * @param trechos
     */
    public void addTrechos(Requisicao requisicao, List<Trecho> trechos) {

        boolean removerDocumentoVeiculoProprio = true;
        
        for (Trecho trecho : trechos) {
            
            if (trecho.isTransporteVeiculoProprio()) {
                removerDocumentoVeiculoProprio = false;
            }
            
            if (trecho.isDeVeiculoProprio() && requisicao.possuiVeiculoProprio()) {

                VeiculoProprio veiculoProprio = requisicao.getDocumentoVeiculoProprio();
                veiculoProprio.getTrechos().add(trecho);
            }

            String tipoTransporte = trecho.getTipoTransporte();

            for (Solicitacao solicitacao : requisicao.getSolicitacoes()) {

                if (!solicitacao.getTrechosIda().contains(trecho) && !solicitacao.getTrechosVolta().contains(trecho)) {

                    if (solicitacao.isDiaria()) {//Se for diaria

                        if (tipoTransporte.equals(ETipoTransporte.ONIBUS.toString()) || tipoTransporte.equals(ETipoTransporte.VEICULO_PROPRIO.toString()) || tipoTransporte.equals(ETipoTransporte.VEICULO_OFICIAL.toString()) || tipoTransporte.equals(ETipoTransporte.AEREO.toString())) {

                            //TODO Adiciona trecho a esta Solicitacao
                            solicitacao.addTrecho(trecho);
                        }

                    } else if (solicitacao.isTransporte()) {//Se for Transporte

                        if (tipoTransporte.equals(ETipoTransporte.ONIBUS.toString()) || tipoTransporte.equals(ETipoTransporte.VEICULO_PROPRIO.toString()) || tipoTransporte.equals(ETipoTransporte.VEICULO_OFICIAL.toString()) || tipoTransporte.equals(ETipoTransporte.AEREO.toString())) {

                            solicitacao.addTrecho(trecho);
                        }

                    } else if (solicitacao.isPassagem()) {//Se tor Passagem

                        if (tipoTransporte.equals(ETipoTransporte.AEREO.toString())) {
                            solicitacao.addTrecho(trecho);
                        }

                    } else {

                        throw new IllegalArgumentException();
                    }

                }

                break;
            }
        }
        
        if (removerDocumentoVeiculoProprio) {
            
            VeiculoProprio documentoVeiculoProprio = requisicao.getDocumentoVeiculoProprio();
            
            if (documentoVeiculoProprio != null) {
                requisicao.setDocumentoVeiculoProprio(null);
                veiculoProprioService.apagar(documentoVeiculoProprio);
            }
        }
    }

    public void aprovaSolicitacao(Solicitacao s) {

        s.setEstadoSolicitacao(EEstadoSolicitacao.APROVADA);

        if (s instanceof SolicitacaoDiaria) {
            solicitacaoDiariaService.atualizar((SolicitacaoDiaria) s);
        }

        if (s instanceof SolicitacaoTransporte) {
            solicitacaoTransporteService.atualizar((SolicitacaoTransporte) s);
        }

        if (s instanceof SolicitacaoPassagem) {
            solicitacaoPassagemService.atualizar((SolicitacaoPassagem) s);
        }

    }

    public void desfazSolicitacao(Solicitacao s) {

        s.setEstadoSolicitacao(EEstadoSolicitacao.SUBMETIDA);

        if (s instanceof SolicitacaoDiaria) {
            solicitacaoDiariaService.atualizar((SolicitacaoDiaria) s);
        }

        if (s instanceof SolicitacaoTransporte) {
            solicitacaoTransporteService.atualizar((SolicitacaoTransporte) s);
        }

        if (s instanceof SolicitacaoPassagem) {
            solicitacaoPassagemService.atualizar((SolicitacaoPassagem) s);
        }

    }

    public void recusaSolicitacao(Solicitacao s) {

        s.setEstadoSolicitacao(EEstadoSolicitacao.RECUSADA);

        if (s instanceof SolicitacaoDiaria) {
            solicitacaoDiariaService.atualizar((SolicitacaoDiaria) s);
        }

        if (s instanceof SolicitacaoTransporte) {
            solicitacaoTransporteService.atualizar((SolicitacaoTransporte) s);
        }

        if (s instanceof SolicitacaoPassagem) {
            solicitacaoPassagemService.atualizar((SolicitacaoPassagem) s);
        }

    }

}
