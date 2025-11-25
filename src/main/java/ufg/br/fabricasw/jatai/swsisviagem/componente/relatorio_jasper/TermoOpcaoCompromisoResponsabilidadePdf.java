package ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 22/07/2019
 */
public class TermoOpcaoCompromisoResponsabilidadePdf extends AbstractJasperReportPdf<TermoOpcaoCompromissoResponsabilidade> {

    private final String JRXML_PATH;

    @Value("${app.config.host}")
    private String HOST;

    public TermoOpcaoCompromisoResponsabilidadePdf() {
        
        super();
        JRXML_PATH = "termo_compromisso/termo_opcao_compromisso_responsabilidade.jrxml";
    }
    
    @Override
    public ByteArrayInputStream gerar(TermoOpcaoCompromissoResponsabilidade termoResponsabilidade) {
        try {
            Map parameters = this.parameters(termoResponsabilidade);
            JRBeanCollectionDataSource dataSource = this.dataSource(termoResponsabilidade);
            
            InputStream jrxml = this.fetchResourceJrxml(JRXML_PATH);
           
            JasperReport report  = JasperCompileManager.compileReport(jrxml);
            JasperPrint  printer = JasperFillManager.fillReport(report, parameters, dataSource);
            
            byte[] buffer = JasperExportManager.exportReportToPdf(printer);
            ByteArrayInputStream pdf = new ByteArrayInputStream(buffer);
            
            return pdf;
            
        } catch (JRException ex) {
            
            Logger.getLogger(RelatorioViagemPdf.class.getName()).log(Level.SEVERE, "->\tERROR CRIAR PDF RELATORIO VIAGEM: \n\n", ex);
        }
        
        return null;
    }
    
    private JRBeanCollectionDataSource dataSource(TermoOpcaoCompromissoResponsabilidade viagemRelatorio) {

        List<TermoOpcaoCompromissoResponsabilidade> beanCollection = new ArrayList<>(1);
        beanCollection.add(viagemRelatorio);

        return new JRBeanCollectionDataSource(beanCollection);
    }
    
    private Map parameters(TermoOpcaoCompromissoResponsabilidade termoResponsabilidade) {

        Map parameters = new HashMap();
        
        Requisicao requisicao = termoResponsabilidade.getVeiculoProprio().getRequisicao();
        Pessoa proposto = requisicao.getProposto();
        String lotacao = (proposto.getUsuario().getUnidade() != null) ? proposto.getUsuario().getUnidade().getNome() : "----";
        LocalDate dataRequisicao = requisicao.getDataRequisicao();
        
        String proponente =  requisicao.getProponente();
        List<Trecho> trechos = requisicao.allTrechos().stream().filter((t) -> t.isDeVeiculoProprio()).collect(Collectors.toList());
        
        String dataExtenso = DateTimeFormatter.ofPattern("'Jataí-GO', dd 'de' MMMM 'de' yyyy", new Locale("pt","BR")).format(dataRequisicao);
 
        parameters.put("HORA_FORMATTER", DateTimeFormatter.ofPattern("HH:mm"));
        parameters.put("DATA_FORMATTER", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        parameters.put("GERADO_EM_STR", this.docGeradoEm());
        parameters.put("RESPONSAVEL", proponente);
        parameters.put("TRECHOS", trechos);
        parameters.put("DATA_EXTENSO", dataExtenso);
        parameters.put("LOTACAO", lotacao);
        parameters.put("HOST", HOST);
        
        return parameters;
    }
}
