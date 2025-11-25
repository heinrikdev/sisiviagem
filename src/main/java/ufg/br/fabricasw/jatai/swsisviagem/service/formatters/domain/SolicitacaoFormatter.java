package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.domain;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoDiaria;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoPassagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.SolicitacaoTransporte;
import ufg.br.fabricasw.jatai.swsisviagem.service.SolicitacaoServiceImpl;

/**
 *
 * @author ronogue
 */
@Service
public class SolicitacaoFormatter implements Formatter<Solicitacao>{
    
    @Autowired
    private SolicitacaoServiceImpl solicitacaoServiceImpl;
    
    @Override
    public String print(Solicitacao object, Locale locale) {
        
        if (object != null && object.getId() != null) {
            
            return object.getId().toString();
            
        } else if (object != null && object.getId() == null) {
            
            return object.getTipoSolicitacao().toString();
            
        } else {
            
            return "";
        }
    }

    @Override
    public Solicitacao parse(String text, Locale locale) throws ParseException {
        
        switch (text) {
            
            case "Diária": {
                
                return new SolicitacaoDiaria();
            }
            case "Transporte": {
                
                return new SolicitacaoTransporte();
            }
            case "Passagem": {
                
                return new SolicitacaoPassagem();
            }
            default: {
                
                Long id = Long.parseLong(text);
                
                Solicitacao solicitacao = this.solicitacaoServiceImpl.find(id);
                return solicitacao;
            }
        }
    }
}
