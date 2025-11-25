package ufg.br.fabricasw.jatai.swsisviagem.componente;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Abastecimento;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ComprovanteViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Justificativa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Motorista;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Passageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RequisicaoPassageiro;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Tarefa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Trecho;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Veiculo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import ufg.br.fabricasw.jatai.swsisviagem.domain.solicitacao.Solicitacao;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemRelatorioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;

@Component
public class RelatorioViagem {

    static ViagemRelatorioService verificacaoService;
    
    static ViagemService viagemService;

    private static Logger log = LoggerFactory.getLogger(RelatorioViagem.class.getName());

    public static final String IMG = "images/PNG_VERTICAL_POSITIVA_SEM_DESCRITOR.png";

    public static final String BRASAO = "images/brasao.jpeg";

    @Autowired
    public RelatorioViagem(ViagemRelatorioService verificacaoService, ViagemService viagemService) {
        RelatorioViagem.verificacaoService = verificacaoService;
        RelatorioViagem.viagemService = viagemService;
    }

    public static ByteArrayInputStream citiesReport(ViagemRelatorio viagemRelatorio) {
        Viagem viagem = viagemRelatorio.getViagem();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter.getInstance(document, out);
            document.open();
            document.addTitle(viagem.getTitulo());
            document.addAuthor("Secretaria de Tecnologia e Informação");

            PdfPTable tabelaCabecario;
            tabelaCabecario = new PdfPTable(3);
            tabelaCabecario.setWidths(new int[]{1, 3, 2});

            Image image = Image.getInstance(IMG);
            Image brasao = Image.getInstance(BRASAO);
            image.scaleAbsolute(100f, 150f);
            brasao.scaleAbsolute(50f, 50f);
            Paragraph paragrafo = new Paragraph();
            tabelaCabecario.addCell(addDadosCelula(brasao));
            //document.add(image);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Serviço Público Federal\nUniversidade Federal de Jataí\n");
            //paragrafo.add("Relatório preliminar da viagem "+viagem.getId().toString());

            tabelaCabecario.addCell(addDadosCelula(paragrafo));
            tabelaCabecario.addCell(addDadosCelula(image));
            //document.add(paragrafo);
            document.add(tabelaCabecario);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("\n\n\nInformações Gerais da Viagem\n\n");
            document.add(paragrafo);
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_JUSTIFIED);
            
            paragrafo.add("Título: " + viagem.getTitulo() + "\n");
            paragrafo.add("Descrição: " + viagem.getDescricao() + "\n\n");
            
            paragrafo.add("Horário estipulado para início: " + viagem.getStartHora() + "\n");
            paragrafo.add("Horário que o motorista encerrou a viagem: " + viagem.getEndHora() + "\n\n");
            paragrafo.add("Informações do(s) motorista(s):\n");
            
            for (Motorista m : viagem.getMotoristas()) {

                paragrafo.add("Nome: " + m.getPessoa().getNome() + "\n");
                paragrafo.add("Habilitação: " + m.getHabilitacao() + "\n");
                paragrafo.add("Email: " + m.getPessoa().getEmail() + "\n");
                paragrafo.add("\n\n");
            }
            
            paragrafo.add("Observações:\n");
            paragrafo.add(viagem.getObservacao() + "\n\n");
            
            document.add(paragrafo);
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Informações do(s) veículo(s)\n\n");
            document.add(paragrafo);
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_LEFT);
            for (Veiculo v : viagem.getVeiculos()) {
                paragrafo.add("Placa: " + v.getPlaca() + "\n");
                paragrafo.add("Modelo: " + v.getModelo() + "\n");
                paragrafo.add("Marca: " + v.getMarca() + "\n");
                paragrafo.add("\n\n");
            }

            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Tarefa(s)\n\n");
            document.add(paragrafo);

            PdfPTable tabelaTarefas;
            tabelaTarefas = new PdfPTable(3);
            tabelaTarefas.setWidths(new int[]{2, 5, 2});
            tabelaTarefas.addCell(addDadosCelula("Título"));
            tabelaTarefas.addCell(addDadosCelula("Descrição"));
            tabelaTarefas.addCell(addDadosCelula("Estado da Tarefa"));
            tabelaTarefas.setWidthPercentage(100);
            for (Tarefa t : viagem.getTarefas()) {
                tabelaTarefas.addCell(t.getTitulo());
                tabelaTarefas.addCell(t.getDescricao());
                tabelaTarefas.addCell(t.getEstadoTarefa().toString());
            }
            document.add(tabelaTarefas);

            PdfPTable tabelaPassageiros;
            tabelaPassageiros = new PdfPTable(3);
            tabelaPassageiros.setWidths(new int[]{5, 2, 2});
            tabelaPassageiros.setWidthPercentage(100);
            tabelaPassageiros.addCell(addDadosCelula("Nome"));
            tabelaPassageiros.addCell(addDadosCelula("Identificação"));
            tabelaPassageiros.addCell(addDadosCelula("Estado do Passageiro"));

            List<RequisicaoPassageiro> passageiros = new ArrayList<>();
            
            viagem.getRequisicaos().forEach(r -> {
                passageiros.addAll(r.getRequisicaoPassageiros());
            });

            for (RequisicaoPassageiro p : passageiros) {
                
                
                tabelaPassageiros.addCell(p.getNome());
                tabelaPassageiros.addCell(p.getIdentificacao());
                tabelaPassageiros.addCell(p.getEstadoPassageiro());
            }

            List<Trecho> trechos = new ArrayList<>();
            viagem.getRequisicaos().forEach(r -> {
                trechos.addAll(r.allTrechos());
            });

            PdfPTable tabelaTrechos;
            tabelaTrechos = new PdfPTable(4);
            tabelaTrechos.setWidths(new int[]{2, 1, 2, 1});
            tabelaTrechos.setWidthPercentage(100);
            tabelaTrechos.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos.addCell(addDadosCelula("Estado Destino"));

            for (Trecho trecho : trechos) {
                if (trecho.getTipoTrecho().equals("Ida")) {
                    tabelaTrechos.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos.addCell(trecho.getCidadeDestino());
                    tabelaTrechos.addCell(trecho.getEstadoDestino());
                }

            }

            PdfPTable tabelaTrechos2;
            tabelaTrechos2 = new PdfPTable(4);
            tabelaTrechos2.setWidths(new int[]{2, 1, 2, 1});
            tabelaTrechos2.setWidthPercentage(100);
            tabelaTrechos2.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Destino"));

            for (Trecho trecho : trechos) {
                if (trecho.getTipoTrecho().equals("Volta")) {
                    tabelaTrechos2.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos2.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos2.addCell(trecho.getCidadeDestino());
                    tabelaTrechos2.addCell(trecho.getEstadoDestino());
                }

            }

            //document.newPage();
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("\n\n\n\nRelação de Passageiros\n\n");
            document.add(paragrafo);
            document.add(tabelaPassageiros);

            document.newPage();
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("\n\n\n\nTrechos de Ida\n\n");
            document.add(paragrafo);
            document.add(tabelaTrechos);
            
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Trechos de Volta\n\n");
            document.add(paragrafo);
            document.add(tabelaTrechos2);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("\n\n\nAbastecimento(s)\n\n");
            document.add(paragrafo);

            PdfPTable tabelaAbastecimento;
            tabelaAbastecimento = new PdfPTable(7);
            tabelaAbastecimento.setWidths(new int[]{2, 2, 2, 2, 2, 2, 3});
            tabelaAbastecimento.setWidthPercentage(100);
            tabelaAbastecimento.addCell(addDadosCelula("Veículo"));
            tabelaAbastecimento.addCell(addDadosCelula("Combustivel"));
            tabelaAbastecimento.addCell(addDadosCelula("Servico"));
            tabelaAbastecimento.addCell(addDadosCelula("Quantidade"));
            tabelaAbastecimento.addCell(addDadosCelula("Odômetro"));
            tabelaAbastecimento.addCell(addDadosCelula("Valor"));
            tabelaAbastecimento.addCell(addDadosCelula("Observações"));
            
            for (Abastecimento ab : viagem.getAbastecimentos()) {
                
                tabelaAbastecimento.addCell(ab.getVeiculo().getPlaca());
                tabelaAbastecimento.addCell(ab.getTipoCombustivel().toString());
                tabelaAbastecimento.addCell(ab.getTipoServico().toString());
                tabelaAbastecimento.addCell(ab.getLitros().toString());
                tabelaAbastecimento.addCell(ab.getOdometro().toString());

                try {
                    tabelaAbastecimento.addCell(ab.getValor().toString());
                } catch (Exception e) {
                    tabelaAbastecimento.addCell("");
                }
                try {

                    tabelaAbastecimento.addCell(ab.getObservacao());
                } catch (Exception e) {
                    tabelaAbastecimento.addCell("");
                }

            }
            document.add(tabelaAbastecimento);

            paragrafo = new Paragraph();
            paragrafo.add("\n\nPara verificar a autenticidade deste documento acesse: https://sisviagem.app.ufj.edu.br/documentos");
            paragrafo.add("\nIdentificador: " + viagemRelatorio.getIdentificador().toString());
            paragrafo.add(" - Código de Verificação: " + viagemRelatorio.getCodigoVerificacao().toString());
            paragrafo.add(" - Data de Emissão: " + viagemRelatorio.getDataEmissao());
            document.add(paragrafo);

        } catch (DocumentException ex) {
            log.info("PDF - Document Exeption : " + ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static PdfPCell addDadosCelula(String dado) {

        PdfPCell celula;

        Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);

        celula = new PdfPCell(new Phrase(dado, headFont));
        celula.setHorizontalAlignment(Element.ALIGN_LEFT);
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celula.setPadding(5);

        return celula;
    }

    private static PdfPCell addDadosCelula(Element dado) {

        PdfPCell celula;


        celula = new PdfPCell();
        celula.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celula.setVerticalAlignment(Element.ALIGN_RIGHT);
        celula.setPadding(5);
        celula.addElement(dado);
        celula.setBorder(PdfPCell.NO_BORDER);
        return celula;
    }

    private static PdfPCell addImagem(Element image, Boolean isLeft) {

        PdfPCell celula;

        celula = new PdfPCell();
        
        if (isLeft) {
            celula.setHorizontalAlignment(Element.ALIGN_LEFT);
        } else {
            celula.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }
        
        celula.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
//        celula.setPadding(5);
        celula.addElement(image);
        celula.setBorder(PdfPCell.NO_BORDER);
        return celula;
    }
    
    public static ByteArrayInputStream veiculo(Requisicao requisicao) {
        
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter.getInstance(document, out);
            document.open();
            document.addTitle(requisicao.getTitulo());
            document.addAuthor("Secretaria de Tecnologia e Informação");

            PdfPTable tabelaCabecario;
            tabelaCabecario = new PdfPTable(3);
            tabelaCabecario.setWidths(new int[]{2, 5, 2});

            Image image = Image.getInstance(IMG);
            Image brasao = Image.getInstance(BRASAO);
            image.scaleAbsolute(100f, 150f);
            brasao.scaleAbsolute(70f, 70f);
            Paragraph paragrafo = new Paragraph();
            tabelaCabecario.addCell(addDadosCelula(brasao));
            //document.add(image);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Serviço Público Federal\nUniversidade Federal de Jataí\n");


            tabelaCabecario.addCell(addDadosCelula(paragrafo));
            tabelaCabecario.addCell(addDadosCelula(image));
            //document.add(paragrafo);
            document.add(tabelaCabecario);

            Paragraph paragrafo1 = new Paragraph();
            paragrafo1.setAlignment(Element.ALIGN_JUSTIFIED);
            paragrafo1.add("\n\nPelo presente eu " + requisicao.getProposto().getNome() + ", lotado(a) no(a), " + requisicao.getUnidade().getNome()
                    + " venho manifestar minha opção por viajar em veículo/condução de minha propriedade/fretado, "
                    + "para percorrer os seguintes trechos:\n\n");

            document.add(paragrafo1);
            PdfPTable tabelaTrechos;
            tabelaTrechos = new PdfPTable(6);
            tabelaTrechos.setWidths(new int[]{1, 1, 2, 1, 1, 2});
            tabelaTrechos.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos.addCell(addDadosCelula("Data Saída"));
            tabelaTrechos.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos.addCell(addDadosCelula("Estado Destino"));
            tabelaTrechos.addCell(addDadosCelula("Data Chegada"));
            
            

            for (Trecho trecho : requisicao.getDocumentoVeiculoProprio().getTrechos()) {

                if (trecho.getTipoTrecho().equals("Ida")) {
                    tabelaTrechos.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos.addCell(trecho.getDataSaida().format(ofPattern));
                    tabelaTrechos.addCell(trecho.getCidadeDestino());
                    tabelaTrechos.addCell(trecho.getEstadoDestino());
                    tabelaTrechos.addCell(trecho.getDataChegada().format(ofPattern));
                }

            }

            ///////////////////////////////////
            PdfPTable tabelaTrechos2;
            tabelaTrechos2 = new PdfPTable(6);
            tabelaTrechos2.setWidths(new int[]{1, 1, 2, 1, 1, 2});
            tabelaTrechos2.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Data Saída"));
            tabelaTrechos2.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Destino"));
            tabelaTrechos2.addCell(addDadosCelula("Data Chegada"));

            for (Trecho trecho : requisicao.getDocumentoVeiculoProprio().getTrechos()) {
                if (trecho.getTipoTrecho().equals("Volta")) {

                    tabelaTrechos2.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos2.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos2.addCell(trecho.getDataSaida().format(ofPattern));
                    tabelaTrechos2.addCell(trecho.getCidadeDestino());
                    tabelaTrechos2.addCell(trecho.getEstadoDestino());
                    tabelaTrechos2.addCell(trecho.getDataChegada().format(ofPattern));
                }

            }

            document.add(tabelaTrechos);
            document.add(tabelaTrechos2);

            Paragraph paragraph = new Paragraph();
            paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
            paragraph.add(" Com o objetivo de comodidade e por minha livre e espontânea vontade,dispensando, assim, "
                    + "a passagem ou o veículo da Universidade colocado à disposição.\n"
                    + "\n"
                    + "Assumo, pelo presente, total e integral "
                    + "responsabilidade por quaisquer ocorrências, acidentes "
                    + "de trânsito ou quaisquer outros, caso venham a acontecer, "
                    + "ficando a Universidade Federal de Jataí totalmente isenta "
                    + "de quaisquer pagamentos, ônus ou responsabilidades por "
                    + "possíveis danos materiais durante a viagem. Comprovar as despesas "
                    + "com alimentação, hospedagem ou abastecimento com Nota ou Cupom Fiscal "
                    + "nominal ao proposto, referente a todos os dias do afastamento inclusive "
                    + "o dia de retorno na cidade de destino (local da missão), até 5 (cinco) dias após o termino da viagem.");

            document.add(paragraph);
            document.close();
        } catch (DocumentException ex) {
            log.info("PDF - Document Exeption : " + ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream comprovanteDerViagem(ComprovanteViagem comprovanteViagem) {
        Requisicao requisicao = comprovanteViagem.getRequisicao();
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        try {
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter.getInstance(document, out);
            document.open();
            document.addTitle(requisicao.getTitulo());
            document.addAuthor("Secretaria de Tecnologia e Informação");

            PdfPTable tabelaCabecario;
            tabelaCabecario = new PdfPTable(3);
            tabelaCabecario.setWidths(new int[]{2, 5, 2});

            Image image = Image.getInstance(IMG);
            Image brasao = Image.getInstance(BRASAO);
            image.scaleAbsolute(100f, 150f);
            brasao.scaleAbsolute(70f, 70f);
            Paragraph paragrafo = new Paragraph();
            tabelaCabecario.addCell(addDadosCelula(brasao));
            //document.add(image);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Serviço Público Federal\nUniversidade Federal Jataí\n");


            tabelaCabecario.addCell(addDadosCelula(paragrafo));
            tabelaCabecario.addCell(addDadosCelula(image));
            //document.add(paragrafo);
            document.add(tabelaCabecario);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Comprovante de Viagem\nVEÍCULO OFICIAL\n\n");

            document.add(paragrafo);

            PdfPTable tabelaTrechos;
            tabelaTrechos = new PdfPTable(3);
            tabelaTrechos.setWidths(new int[]{1, 1, 1});
            tabelaTrechos.addCell(addDadosCelula(" "));
            tabelaTrechos.addCell(addDadosCelula("Origem"));
            tabelaTrechos.addCell(addDadosCelula("Destino"));

            for (Trecho trecho : requisicao.allTrechos()) {

                if (trecho.getTipoTrecho().equals("Ida")) {
                    tabelaTrechos.addCell("Trecho de IDA");
                    tabelaTrechos.addCell(trecho.getCidadeOrigem() + "-" + trecho.getEstadoOrigem());
                    tabelaTrechos.addCell(trecho.getCidadeDestino() + "-" + trecho.getEstadoDestino());
                }

            }
            String dataTermino = "";

            for (Trecho trecho : requisicao.allTrechos()) {

                if (trecho.getTipoTrecho().equals("Volta")) {
                    tabelaTrechos.addCell("Trecho de VOLTA");
                    tabelaTrechos.addCell(trecho.getCidadeOrigem() + "-" + trecho.getEstadoOrigem());
                    tabelaTrechos.addCell(trecho.getCidadeDestino() + "-" + trecho.getEstadoDestino());
                    dataTermino = trecho.getDataChegada().format(ofPattern);

                }

            }

            tabelaTrechos.addCell("Período Realizado");
            tabelaTrechos.addCell("Início\n" + requisicao.getDataRequisicao());
            tabelaTrechos.addCell("Término\n" + dataTermino);

            document.add(tabelaTrechos);

            Paragraph paragraph = new Paragraph();
            paragraph.add("\n\n\n\n");
            document.add(paragraph);

            Paragraph paragraph1 = new Paragraph();
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            paragraph1.add(requisicao.getProposto().getNome() + "\n");
            paragraph1.add("________________________________________________\n");
            paragraph1.add("Documento assinado digitalmente por meio de senha e usuário\n\n\n");
            //paragraph1.add();
            document.add(paragraph1);

            Viagem viagem = new Viagem();
            for (Viagem v : viagemService.findAll()) {
                if (v.getRequisicaos().contains(requisicao)) {
                    viagem = v;
                    break;
                }

            }

            String data = new String();
            for (Trecho t : comprovanteViagem.getRequisicao().allTrechos()) {
                if (t.getTipoTrecho().equals("Volta")) {
                    data = t.getDataChegada().format(ofPattern);
                }

            }

            PdfPTable tabelaProposto;
            tabelaProposto = new PdfPTable(1);
            tabelaProposto.addCell("MISSÃO CUMPRIDA\nDATA: " + data + "\n\n\n\n"
                    + "\n\n" + "                                " + "________________________________\n"
                    + "                                     " + "                   Proponente\n\n");

            document.add(tabelaProposto);
            document.add(new Paragraph("\n\n"));

            PdfPTable tableVeiculo;
            tableVeiculo = new PdfPTable(1);
            //tableVeiculo.setWidths(new int[]{});
            if (requisicao.getDocumentoVeiculoProprio() != null) {
                tableVeiculo.addCell("VEÍCULO OFICIAL()                     VEÍCULO TERCEIRIZADO(X)");
                tableVeiculo.addCell("VEÍCULO: " + requisicao.getDocumentoVeiculoProprio().getModelo()
                        + "                      Placa: " + requisicao.getDocumentoVeiculoProprio().getPlaca());
                tableVeiculo.addCell("MOTORISTA: Não Consta");
            } else {
                viagem = new Viagem();
                for (Viagem v : viagemService.findAll()) {
                    if (v.getRequisicaos().contains(requisicao)) {
                        viagem = v;
                        break;
                    }

                }
                try {
                    tableVeiculo.addCell("VEÍCULO OFICIAL(X)                     VEÍCULO TERCEIRIZADO()");
                    tableVeiculo.addCell("VEÍCULO: " + viagem.getVeiculos().get(0).getCarro()
                            + "                      Placa: " + viagem.getVeiculos().get(0).getPlaca());
                    tableVeiculo.addCell("MOTORISTA: " + viagem.getMotoristas().get(0).getPessoa().getNome());
                } catch (Exception e) {

                }

            }
            document.add(tableVeiculo);

            paragrafo = new Paragraph();
            paragrafo.add("\n\nPara verificar a autenticidade deste documento acesse: https://sisviagem.app.ufj.edu.br/documentos");
            paragrafo.add(" - Identificador: " + comprovanteViagem.getIdentificador().toString());
            paragrafo.add(" - Código de Verificação: " + comprovanteViagem.getCodigoVerificacao().toString());
            paragrafo.add(" - Data de Emissão: " + comprovanteViagem.getDataEmissao());
            document.add(paragrafo);

        } catch (DocumentException ex) {
            log.info("PDF - Document Exeption : " + ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());

    }

    public static ByteArrayInputStream justificativa(Justificativa justificativa) {
        Requisicao requisicao = justificativa.getRequisicao();
        String motivo = justificativa.getMotivo();
        String pessoa = justificativa.getPessoa();

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter.getInstance(document, out);
            document.open();
            document.addTitle(requisicao.getTitulo());
            document.addAuthor("Secretaria de Tecnologia e Informação");

            PdfPTable tabelaCabecario;
            tabelaCabecario = new PdfPTable(3);
            tabelaCabecario.setWidths(new int[]{2, 5, 2});

            Image image = Image.getInstance(IMG);
            Image brasao = Image.getInstance(BRASAO);
            image.scaleAbsolute(100f, 150f);
            brasao.scaleAbsolute(70f, 70f);
            Paragraph paragrafo = new Paragraph();
            tabelaCabecario.addCell(addImagem(brasao, true));
            //document.add(image);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Serviço Público Federal\nUniversidade Federal de Jataí\n");

            tabelaCabecario.addCell(addDadosCelula(paragrafo));
            
            tabelaCabecario.addCell(addImagem(image, false));
            //document.add(paragrafo);
            document.add(tabelaCabecario);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);

            paragrafo.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD));
            paragrafo.add("\n\nJustificativa\n\n");
            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_LEFT);
            //paragrafo.setFont(headFont);
            paragrafo.add("Assunto:  Concessão diária fora do prazo legal de quinze dias de antecedência.\n\n");
            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_JUSTIFIED);
            //paragrafo.setFont(headFont);
            paragrafo.add("Venho justificar o pedido de concessão de diárias para o(a) ");

            //        requisicao.getProposto().getNome()+" fora do prazo legal de quinze dias de antecedência devido a(o) "+
            //motivo+ ".\n\n");
            for (int i = 0; i < requisicao.getPassageiros().size(); i++) {
                if (i != requisicao.getPassageiros().size() - 1) {
                    paragrafo.add(requisicao.getPassageiros().get(i).getNome() + ", ");
                } else {
                    paragrafo.add(requisicao.getPassageiros().get(i).getNome() + " ");
                }
            }
            paragrafo.add("fora do prazo legal de quinze dias de antecedência devido a(o) " + motivo + ".\n\n");
            document.add(paragrafo);
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_RIGHT);

            /*Date date = new Date();
            DateFormat df;
            Locale BRAZIL = new Locale("pt","BR");
            df = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL);*/
            paragrafo.add(justificativa.getDataFull());

            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            
            String assinaturaObservacao = "\n\n\nDocumento assinado eletronicamente por: " + pessoa + " em " + justificativa.getDataEmissao() + " com fundamento no art. 6º, § 1º, do " +
                                           "Decreto nº 8.539, de 8 de outubro de 2015.";
            
            paragrafo.add(assinaturaObservacao);
            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.add("\n\n\n\n\n\nPara verificar a autenticidade deste documento acesse: https://sisviagem.app.ufj.edu.br/documentos");
            paragrafo.add(" - Identificador: " + justificativa.getIdentificador().toString());
            paragrafo.add(" - Código de Verificação: " + justificativa.getCodigoVerificacao().toString());
            paragrafo.add(" - Data de Emissão: " + justificativa.getDataEmissao());
            document.add(paragrafo);

        } catch (DocumentException ex) {
            log.info("PDF - Document Exeption : " + ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream formulario(Requisicao requisicao) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Font headFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
            PdfWriter.getInstance(document, out);
            document.open();
            document.addTitle("FORMULÁRIO DE DIÁRIAS E TRANSPORTE");
            document.addAuthor("Secretaria de Tecnologia e Informação");

            PdfPTable tabelaCabecario;
            tabelaCabecario = new PdfPTable(3);
            tabelaCabecario.setWidths(new int[]{2, 5, 2});

            Image image = Image.getInstance(IMG);
            Image brasao = Image.getInstance(BRASAO);
            image.scaleAbsolute(100f, 150f);
            brasao.scaleAbsolute(70f, 70f);
            Paragraph paragrafo = new Paragraph();
            tabelaCabecario.addCell(addDadosCelula(brasao));
            //document.add(image);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Serviço Público Federal\nUniversidade Federal de Jataí\n");


            tabelaCabecario.addCell(addDadosCelula(paragrafo));
            tabelaCabecario.addCell(addDadosCelula(image));
            //document.add(paragrafo);
            document.add(tabelaCabecario);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.setFont(headFont);
            paragrafo.add("\n");
            paragrafo.add("Formulário de Diárias e Transporte\n\n");

            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_RIGHT);
            paragrafo.setFont(headFont);
            //paragrafo.add("");
            paragrafo.add(requisicao.getDataRequisicao() + "\n\n");
            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_LEFT);
            boolean ta = false;
            for (Solicitacao s : requisicao.getSolicitacoes()) {
                if (s.isDiaria()) {
                    paragrafo.add("DIÁRIAS: SIM     ");
                    ta = true;
                    break;
                }

            }

            if (!ta) {
                paragrafo.add("DIÁRIAS: NÃO     ");
            }

            ta = false;
            for (Solicitacao s : requisicao.getSolicitacoes()) {
                if (s.isPassagem()) {
                    paragrafo.add(" - PASSAGEM: SIM\n\n");
                    ta = true;
                    break;
                }

            }

            if (!ta) {
                paragrafo.add(" - PASSAGEM: NÃO\n\n");
            }

            document.add(paragrafo);

            PdfPTable tabelaRequerimento;
            tabelaRequerimento = new PdfPTable(1);
            tabelaRequerimento.setWidthPercentage(100);
            tabelaRequerimento.setWidths(new int[]{1});

            paragrafo = new Paragraph("Requerimento e Justificativa");
            //paragrafo.setIndentationLeft(20);
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            PdfPCell t = new PdfPCell();
            t.setIndent(PdfPCell.ALIGN_CENTER);
            t.addElement(paragrafo);
            tabelaRequerimento.addCell(t);
            tabelaRequerimento.addCell(requisicao.getTitulo() + "\n" + requisicao.getJustificativa());

            document.add(tabelaRequerimento);

            List<Trecho> trechos = requisicao.allTrechos();

            PdfPTable tabelaTrechos;
            tabelaTrechos = new PdfPTable(4);
            tabelaTrechos.setWidthPercentage(100);
            tabelaTrechos.setWidths(new int[]{2, 1, 2, 1});
            tabelaTrechos.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos.addCell(addDadosCelula("Estado Destino"));

            for (Trecho trecho : trechos) {
                if (trecho.getTipoTrecho().equals("Ida")) {
                    tabelaTrechos.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos.addCell(trecho.getCidadeDestino());
                    tabelaTrechos.addCell(trecho.getEstadoDestino());
                }

            }

            PdfPTable tabelaTrechos2;
            tabelaTrechos2 = new PdfPTable(4);
            tabelaTrechos2.setWidthPercentage(100);
            tabelaTrechos2.setWidths(new int[]{2, 1, 2, 1});
            tabelaTrechos2.addCell(addDadosCelula("Cidade Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Origem"));
            tabelaTrechos2.addCell(addDadosCelula("Cidade Destino"));
            tabelaTrechos2.addCell(addDadosCelula("Estado Destino"));

            for (Trecho trecho : trechos) {
                if (trecho.getTipoTrecho().equals("Volta")) {
                    tabelaTrechos2.addCell(trecho.getCidadeOrigem());
                    tabelaTrechos2.addCell(trecho.getEstadoOrigem());
                    tabelaTrechos2.addCell(trecho.getCidadeDestino());
                    tabelaTrechos2.addCell(trecho.getEstadoDestino());
                }

            }

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("\n");
            document.add(paragrafo);

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Propostos");
            PdfPCell a1 = new PdfPCell();
            a1.addElement(paragrafo);
            a1.setIndent(PdfPCell.ALIGN_CENTER);
            PdfPTable tmp = new PdfPTable(1);
            tmp.setWidthPercentage(100);
            tmp.addCell(a1);
            document.add(tmp);

            PdfPTable tabelaPassageiros;
            tabelaPassageiros = new PdfPTable(4);
            tabelaPassageiros.setWidthPercentage(100);
            tabelaPassageiros.setWidths(new int[]{2, 1, 1, 1});
            tabelaPassageiros.addCell(addDadosCelula("NOME"));
            tabelaPassageiros.addCell(addDadosCelula("CPF/SIAPE"));
            tabelaPassageiros.addCell(addDadosCelula("RG"));
            tabelaPassageiros.addCell(addDadosCelula("TELEFONE/E-MAIL"));

            for (Passageiro p : requisicao.getPassageiros()) {
                tabelaPassageiros.addCell(p.getNome());
                tabelaPassageiros.addCell(p.getIdentificacao());
                if (p.getRg() != null) {
                    tabelaPassageiros.addCell(p.getRg() + "/" + p.getOrgao());
                } else {
                    tabelaPassageiros.addCell("");
                }
                tabelaPassageiros.addCell(p.getTelefone());
            }

            document.add(tabelaPassageiros);
            document.add(new Paragraph("\n"));

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Dados do Solicitante");
            a1 = new PdfPCell();
            a1.addElement(paragrafo);
            a1.setIndent(PdfPCell.ALIGN_CENTER);
            tmp = new PdfPTable(1);
            tmp.setWidthPercentage(100);
            tmp.addCell(a1);
            document.add(tmp);

            PdfPTable tabelaSolicitante;
            tabelaSolicitante = new PdfPTable(2);
            tabelaSolicitante.setWidthPercentage(100);
            tabelaSolicitante.setWidths(new int[]{1, 1});
            tabelaSolicitante.addCell(addDadosCelula("NOME DO SOLICITANTE"));
            tabelaSolicitante.addCell(addDadosCelula(requisicao.getProposto().getNome()));
            tabelaSolicitante.addCell(addDadosCelula("CPF"));
            tabelaSolicitante.addCell(addDadosCelula(requisicao.getProposto().getCpf()));
            //tabelaSolicitante.addCell(addDadosCelula("CPF"));
            //tabelaSolicitante.addCell(addDadosCelula(requisicao.getProposto().getCpf()));
            tabelaSolicitante.addCell(addDadosCelula("E-MAIL"));
            tabelaSolicitante.addCell(addDadosCelula(requisicao.getProposto().getEmail()));
            document.add(tabelaSolicitante);

            document.add(new Paragraph("\n"));

            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Trecho de Ida");
            a1 = new PdfPCell();
            a1.addElement(paragrafo);
            a1.setIndent(PdfPCell.ALIGN_CENTER);
            tmp = new PdfPTable(1);
            tmp.setWidthPercentage(100);
            tmp.addCell(a1);
            document.add(tmp);
            document.add(tabelaTrechos);
 
            paragrafo = new Paragraph();
            paragrafo.setAlignment(Element.ALIGN_CENTER);
            paragrafo.add("Trecho de Volta");
            a1 = new PdfPCell();
            a1.addElement(paragrafo);
            a1.setIndent(PdfPCell.ALIGN_CENTER);
            tmp = new PdfPTable(1);
            tmp.setWidthPercentage(100);
            tmp.addCell(a1);
            document.add(tmp);
            document.add(tabelaTrechos2);

            paragrafo = new Paragraph();

            paragrafo.add("\n\n");

            document.add(paragrafo);

            Paragraph paragraph1 = new Paragraph();
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            paragraph1.add("\n\n\n");
            paragraph1.add("                    ________________________________________________\n");
            paragraph1.add("                                Assinatura e Carimbo da Chefia/Direção\n\n\n");
            //paragraph1.add();
            PdfPTable tabelaAssinatura;
            tabelaAssinatura = new PdfPTable(1);
            tabelaAssinatura.setWidths(new int[]{1});
            PdfPCell t1 = new PdfPCell(paragraph1);
            t1.setIndent(PdfPCell.ALIGN_CENTER);
            tabelaAssinatura.addCell(t1);
            document.add(tabelaAssinatura);

        } catch (DocumentException ex) {
            log.info("PDF - Document Exeption : " + ex);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

}
