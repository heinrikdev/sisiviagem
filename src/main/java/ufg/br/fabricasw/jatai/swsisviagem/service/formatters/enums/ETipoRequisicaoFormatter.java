/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums;

import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Service;
import ufg.br.fabricasw.jatai.swsisviagem.domain.enums.ETipoRequisicao;

/**
 *
 * @author ronogue
 */
@Service
public class ETipoRequisicaoFormatter implements Formatter<ETipoRequisicao>{

    @Override
    public String print(ETipoRequisicao obj, Locale locale) {
        return obj != null ? obj.id().toString() : "";
    }

    @Override
    public ETipoRequisicao parse(String text, Locale locale) throws ParseException {
        Integer id = Integer.valueOf(text);
        return ETipoRequisicao.values()[id];
    }
}
