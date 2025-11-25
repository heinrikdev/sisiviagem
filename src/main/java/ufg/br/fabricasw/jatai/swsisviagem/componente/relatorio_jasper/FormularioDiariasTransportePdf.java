package ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioDiariasTransportePdf {

    private static final String TEMPLATE =
            "/jasper_reports/diarias_transporte/formulario.jrxml";

    public ByteArrayInputStream gerar(Requisicao req)
            throws JRException, IOException {
        // 1) Compila o JRXML
        InputStream jrxml =
                getClass().getResourceAsStream(TEMPLATE);
        JasperReport report =
                JasperCompileManager.compileReport(jrxml);

        List<ItemItinerario> itinerarios = Arrays.asList(
                new ItemItinerario("15/08/2025", "Jataí-GO", "Brasília-DF", "Ônibus"),
                new ItemItinerario("16/08/2025", "Brasília-DF", "São Paulo-SP", "Avião"),
                new ItemItinerario("17/08/2025", "São Paulo-SP", "Rio de Janeiro-RJ", "Ônibus"),
                new ItemItinerario("18/08/2025", "Rio de Janeiro-RJ", "Jataí-GO", "Avião")
                // Pode adicionar quantas linhas precisar!
        );
        // Criar DataSource para itinerário
        JRDataSource itinerarioDataSource = new JRBeanCollectionDataSource(itinerarios);

        // 2) Mapeia parâmetros
        Map<String,Object> params = new HashMap<>();
        params.put("proponente", req.getProponente());
        params.put("lotacao",    req.getTitulo());
        params.put("cpf",        req.getPassageiros().get(0).getIdentificacao());


        params.put("data_saida", req.getTrechosDeIda().get(0).getDataSaida()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("data_retorno", req.getTrechosDeVolta().get(0).getDataChegada()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("destino", req.getTrechosDeIda().get(0).getCidadeDestino());

        // … outros campos do seu formulário

        // 3) Sem datasource de linhas
        JRDataSource ds = new JREmptyDataSource();

        Map<String, Object> parametros = new HashMap<>();

// Dados pessoais
        parametros.put("assinadoPeloProponenteEm", req.getDataSubmissao().toLocalDate());
        parametros.put("tipoServidor", "true");

        parametros.put("nomeCompleto", "MARCOS WAGNER DE SOUZA RIBEIRO");
        parametros.put("dataNascimento", "14/07/1980");
        parametros.put("nomeMae", "MARIA DA SILVA RIBEIRO");
        parametros.put("cpf", "123.456.789-00");
        parametros.put("rg", "12.345.678");
        parametros.put("passaporte", "AB123456");
        parametros.put("matriculaSiape", "1234567");
        parametros.put("cargoFuncao", "PROFESSOR ADJUNTO");
        parametros.put("lotacaoOrgao", "UNIVERSIDADE FEDERAL DE JATAÍ");
        parametros.put("telefone", "(64) 1234-5678");
        parametros.put("celular", "(64) 99999-9999");
        parametros.put("email", "marcos@ufj.edu.br");

// Dados bancários
        parametros.put("banco", "001 - BANCO DO BRASIL");
        parametros.put("agencia", "1234-5");
        parametros.put("conta", "12345-6");

// Dados da viagem
        parametros.put("objetoViagem", "Participação em congresso acadêmico sobre educação");
        parametros.put("dataInicioMissao", "15/08/2025");
        parametros.put("dataTerminoMissao", "18/08/2025");

        parametros.put("horaInicioMissao", "08:00");
        parametros.put("tempoEstimadoIda", "2h30min");
        parametros.put("horaFimMissao", "17:00");
        parametros.put("tempoEstimadoVolta", "3h15min");

        parametros.put("opcaoDiarias", "true");
        parametros.put("RESPONSAVEL_UNIDADE", req.getUnidade().getNome());       // nunca null

        parametros.put("RESPONSAVEL", req.getUnidade().getUsuario().getPessoa().getNome());       // nunca null
        parametros.put("dataEmissao", LocalDate.now());               // ou req.getDataEmissao()
        parametros.put("DATA_FORMATTER", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // 4) Preenche e exporta
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(report, parametros, itinerarioDataSource);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

        return new ByteArrayInputStream(baos.toByteArray());
    }
}