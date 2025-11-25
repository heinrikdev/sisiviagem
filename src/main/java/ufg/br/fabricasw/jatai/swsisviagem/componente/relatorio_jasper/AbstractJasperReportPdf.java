package ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ronogue
 */
public abstract class AbstractJasperReportPdf<T> {
    
    public AbstractJasperReportPdf() {}
    
    abstract public ByteArrayInputStream gerar(T t);
    
    protected InputStream fetchResourceJrxml(String filename) {
        
        InputStream jrxml = getClass().getResourceAsStream(String.format("/jasper_reports/%s", filename));
        
        return jrxml;
    }
    
    protected String docGeradoEm() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return formatter.format(now);
    }
}
