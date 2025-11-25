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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Pessoa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RelatorioViagemRequisicaoProposto;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;

/**
 *
 * @author Ronaldo N Sousa
 * Criado em: 22/07/2019
 */
public class RelatorioViagemRequisicaoPropostoPdf extends AbstractJasperReportPdf<RelatorioViagemRequisicaoProposto> {

    private final String JRXML_PATH;

    @Value("${app.config.host}")
    private String HOST;

    public RelatorioViagemRequisicaoPropostoPdf() {
        
        super();
        JRXML_PATH = "relatorio_requisicao/detalhes_requisicao.jrxml";
    }
    
    @Override
    public ByteArrayInputStream gerar(RelatorioViagemRequisicaoProposto termoResponsabilidade) {
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
    
    private JRBeanCollectionDataSource dataSource(RelatorioViagemRequisicaoProposto viagemRelatorio) {

        List<RelatorioViagemRequisicaoProposto> beanCollection = new ArrayList<>(1);
        beanCollection.add(viagemRelatorio);

        return new JRBeanCollectionDataSource(beanCollection);
    }
    
    private Map parameters(RelatorioViagemRequisicaoProposto termoResponsabilidade) {

        Map parameters = new HashMap();
        
        Requisicao requisicao = termoResponsabilidade.getRequisicao();
        Pessoa proposto = requisicao.getProposto();
        
        String unidade = (proposto.getUsuario().getUnidade() != null) ? proposto.getUsuario().getUnidade().getNome() : "----";
        LocalDate dataRequisicao = requisicao.getDataRequisicao();
        
        String dataExtenso = DateTimeFormatter.ofPattern("'Jataí-GO', dd 'de' MMMM 'de' yyyy", new Locale("pt","BR")).format(dataRequisicao);
 
        parameters.put("HORA_FORMATTER", DateTimeFormatter.ofPattern("HH:mm"));
        parameters.put("DATA_FORMATTER", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        parameters.put("GERADO_EM_STR", this.docGeradoEm());
        parameters.put("DATA_EXTENSO", dataExtenso);
        parameters.put("UNIDADE_DEPARTAMENTO", unidade);
        parameters.put("PROPOSTO", proposto);
        parameters.put("TRECHO_IDA", requisicao.getTrechosDeIda());
        parameters.put("TRECHO_VOLTA", requisicao.getTrechosDeVolta());
        parameters.put("MOTIVO_VIAGEM", StringUtils.capitalize(requisicao.getTitulo().trim()));
        parameters.put("HOST", HOST);
                
        String descricaoViagem;
        
        if (requisicao.getDescricaoAtividadesDesenvolvidas() != null) {
            
            descricaoViagem = requisicao.getDescricaoAtividadesDesenvolvidas().trim();
            
        } else {
            
            descricaoViagem = requisicao.getJustificativa().trim();
        }
        
        parameters.put("DESCRICAO_VIAGEM", StringUtils.capitalize(descricaoViagem));

        return parameters;
    }
}
