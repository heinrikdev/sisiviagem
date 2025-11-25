package ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ronogue
 */
@Component
public class RelatorioViagemPdf extends AbstractJasperReportPdf<ViagemRelatorio> {

    private final String JRXML_PATH;
    private final String JRXML_PATH_PASSAGEIROS_TRECHOS_ABASTECIMENTOS;

    @Value("${app.config.host}")
    private String HOST;

    public RelatorioViagemPdf() {

        super();
        JRXML_PATH = "relatorio_viagem/detalhes_viagem.jrxml";
        JRXML_PATH_PASSAGEIROS_TRECHOS_ABASTECIMENTOS = "relatorio_viagem/passageiros_trechos_abastecimentos.jrxml";
    }

    @Override
    public ByteArrayInputStream gerar(ViagemRelatorio relatorioViagem) {

        try {
            Map parameters = this.parameters(relatorioViagem);
            Map passageiros = this.parametersSecondary(relatorioViagem);

            JasperReport principal = JasperCompileManager.compileReport(this.fetchResourceJrxml(JRXML_PATH));
            JasperReport secundario = JasperCompileManager
                    .compileReport(this.fetchResourceJrxml(JRXML_PATH_PASSAGEIROS_TRECHOS_ABASTECIMENTOS));

            List<JasperPrint> jasperPrintList = new ArrayList<>();

            JasperPrint jasperPrint1 = JasperFillManager.fillReport(principal, parameters,
                    this.dataSource(relatorioViagem));
            jasperPrintList.add(jasperPrint1);

            JasperPrint jasperPrint2 = JasperFillManager.fillReport(secundario, passageiros,
                    this.dataSource(relatorioViagem));
            jasperPrintList.add(jasperPrint2);

            JRPdfExporter exporter = new JRPdfExporter();

            OutputStreamPdf outputStreamPdf = new OutputStreamPdf();

            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
            exporter.setExporterOutput(outputStreamPdf);
            exporter.exportReport();

            byte[] buffer = outputStreamPdf.getByteArray();

            return new ByteArrayInputStream(buffer);

        } catch (JRException ex) {

            Logger.getLogger(RelatorioViagemPdf.class.getName()).log(Level.SEVERE,
                    "->\tERROR CRIAR PDF RELATORIO VIAGEM: \n\n", ex);
        }

        return null;
    }

    private JRBeanCollectionDataSource dataSource(ViagemRelatorio viagemRelatorio) {

        List<ViagemRelatorio> beanCollection = new ArrayList<>(1);
        beanCollection.add(viagemRelatorio);

        return new JRBeanCollectionDataSource(beanCollection);
    }

    private Map parametersSecondary(ViagemRelatorio relatorioViagem) {

        Map parameters = new HashMap();

        Viagem viagem = relatorioViagem.getViagem();

        parameters.put("HORA_FORMATTER", DateTimeFormatter.ofPattern("HH:mm"));
        parameters.put("DATA_FORMATTER", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        parameters.put("GERADO_EM_STR", this.docGeradoEm());

        parameters.put("PASSAGEIROS", viagem.getRequisicaoPassageiros());
        parameters.put("TRECHOS_IDA", viagem.getTrechosDeIda());
        parameters.put("TRECHOS_VOLTA", viagem.getTrechosDeVolta());
        parameters.put("ABASTECIMENTOS", viagem.getAbastecimentos());
        parameters.put("HOST",HOST);

        return parameters;
    }

    private Map parameters(ViagemRelatorio relatorioViagem) {

        Map parameters = new HashMap();

        Viagem viagem = relatorioViagem.getViagem();

        parameters.put("HORA_FORMATTER", DateTimeFormatter.ofPattern("HH:mm"));
        parameters.put("DATA_FORMATTER", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        parameters.put("GERADO_EM_STR", this.docGeradoEm());
        parameters.put("VIAGEM", relatorioViagem.getViagem());
        parameters.put("MOTORISTAS", viagem.getMotoristas());
        parameters.put("TAREFAS", viagem.getTarefas());
        parameters.put("VEICULOS", viagem.getVeiculos());
        parameters.put("HOST",HOST);

        return parameters;
    }

    private class OutputStreamPdf implements OutputStreamExporterOutput {

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        @Override
        public OutputStream getOutputStream() {
            return pdfOutputStream;
        }

        public byte[] getByteArray() {
            return this.pdfOutputStream.toByteArray();
        }

        @Override
        public void close() {
            try {
                this.pdfOutputStream.close();

            } catch (IOException ex) {
                Logger.getLogger(RelatorioViagemPdf.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
