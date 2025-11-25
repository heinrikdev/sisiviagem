package ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao;

import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoSolicitacao;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by thevilela on 18/03/17.
 */
@Entity
@DiscriminatorValue(value = "DIARIA")
public class SolicitacaoDiaria extends Solicitacao {

    public SolicitacaoDiaria() {
        super(ETipoSolicitacao.DIARIA);
    }

}
