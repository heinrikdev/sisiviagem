package ufg.br.fabricasw.jatai.swsisviagem.componente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Component;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;

/**
 *
 * @author ronogue
 */
@Component
public class GeneratePdfViagemInfoBasicas {
    
    private String path; //Caminho base
    
    private String pathToReportPackage; // Caminho para o package onde estão armazenados os relatorios Jarper
	
    //Recupera os caminhos para que a classe possa encontrar os relatórios
    public GeneratePdfViagemInfoBasicas() {
        this.path = this.getClass().getClassLoader().getResource("").getPath();
        this.pathToReportPackage ="jasper-report-templates/";
        System.out.println(path);
    }
	
    //Imprime/gera uma lista de Clientes
    public void imprimir(Viagem viagem){
        
        try {
            JasperReport report = JasperCompileManager.compileReport(this.getPathToReportPackage() + "viagem_relatorio.jrxml");
            
            Map<String, Object> dataSource = new HashMap<>();
            
            dataSource.put("Viagem", viagem);
            dataSource.put("motoristas", viagem.getMotoristas());
            
            JasperPrint print = JasperFillManager.fillReport(report, dataSource);
            
            List<Viagem> clientes = new ArrayList<>();
            clientes.add(viagem);
            
            //JasperPrint print = JasperFillManager.fillReport(report, null, new JRBeanCollectionDataSource(clientes));
            JasperExportManager.exportReportToPdfFile(print, "Relatorio_de_Clientes.pdf");
            
        } catch (JRException ex) {
            
            System.out.println("\n\nERROR: " + ex.getMessage() + "\n\n");
        }
    }

    public String getPathToReportPackage() {
        return this.pathToReportPackage;
    }

    public String getPath() {
        return this.path;
    }
}
