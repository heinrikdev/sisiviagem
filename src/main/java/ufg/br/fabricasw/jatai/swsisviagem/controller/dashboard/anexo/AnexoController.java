package ufg.br.fabricasw.jatai.swsisviagem.controller.dashboard.anexo;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xml.sax.SAXException;
import ufg.br.fabricasw.jatai.swsisviagem.componente.RelatorioViagem;
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.FormularioDiariasTransportePdf;
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.RelatorioViagemPdf;
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.RelatorioViagemRequisicaoPropostoPdf;
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.TermoOpcaoCompromisoResponsabilidadePdf;
import ufg.br.fabricasw.jatai.swsisviagem.controller.ValidacaoDocumentoController;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RelatorioViagemRequisicaoProposto;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.VeiculoProprio;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.Viagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import ufg.br.fabricasw.jatai.swsisviagem.repository.RelatorioViagemRequisicaoPropostoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.AnexoService;
import ufg.br.fabricasw.jatai.swsisviagem.service.JustificativaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.RequisicaoServiceImpl;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemRelatorioService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemService;
import ufg.br.fabricasw.jatai.swsisviagem.storage.FileSystemStorageService;

/**
 * 
 * Controller para exibir anexos.
 * 
 * @author Ronaldo N. de Sousa
 */
@Controller("anexo_files")
@RequestMapping("/dashboard/anexo")
public class AnexoController {

    @Autowired
    private AnexoService anexoService;

    @Autowired
    private ViagemService viagemService;

    @Autowired
    private ViagemRelatorioService viagemRelatorioService;

    @Autowired
    private RelatorioViagemPdf relatorioViagemPdf;

    @Autowired
    private FileSystemStorageService fileSystemStorageService;

    @Autowired
    private RelatorioViagemRequisicaoPropostoRepository relatorioViagemPropostoRepository;

    @Autowired
    private RequisicaoServiceImpl requisicaoService;

    @Autowired
    private JustificativaService justificativaService;

    @GetMapping(value = "/{anexo_id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> baixarAnexo(@PathVariable("anexo_id") Long anexo_id)
            throws SAXException, TikaException {
        try {
            Anexo anexo = this.anexoService.find(anexo_id);
            Resource resource = this.fileSystemStorageService.loadAsResource(anexo.getCaminho());
            InputStreamResource arquivo = new InputStreamResource(resource.getInputStream());
            String mimeType = this.findMimeType(resource);
            String filename = anexo.getNome() + this.getFileExtension(resource.getFilename());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=\"" + filename + "\"");
            headers.add("Content-Type", mimeType);

            return ResponseEntity.ok().headers(headers).body(arquivo);

        } catch (IOException ex) {

            Logger.getLogger(AnexoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{requisicao_id}/documento_veiculo_proprio", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> chamadaReport(@PathVariable("requisicao_id") Long requisicao_id)
            throws IOException {

        Requisicao requisicao = requisicaoService.find(requisicao_id);
        VeiculoProprio veiculoProprio = requisicao.getDocumentoVeiculoProprio();

        ByteArrayInputStream bis;

        if (veiculoProprio.hasTermoResponsabilidade()) {

            TermoOpcaoCompromissoResponsabilidade termoResponsabilidade = veiculoProprio.getTermoResponsabilidade();
            TermoOpcaoCompromisoResponsabilidadePdf pdf = new TermoOpcaoCompromisoResponsabilidadePdf();

            bis = pdf.gerar(termoResponsabilidade);

        } else {

            TermoOpcaoCompromissoResponsabilidade termoResponsabilidade = new TermoOpcaoCompromissoResponsabilidade(1L,
                    0, LocalDate.now(), 3L, veiculoProprio);

            TermoOpcaoCompromisoResponsabilidadePdf pdf = new TermoOpcaoCompromisoResponsabilidadePdf();

            bis = pdf.gerar(termoResponsabilidade);
            // bis = RelatorioViagem.veiculo(requisicao);
        }

        final String filename = "inline; filename=\"" + requisicao_id + "-Termo de responsabilidade-"
                + requisicao.getProponente() + ".pdf\"";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping(
            value = "/{requisicao_id}/formulario_diarias_transporte",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<InputStreamResource> downloadFormulario(
            @PathVariable("requisicao_id") Long requisicao_id)
            throws IOException, JRException {

        Requisicao req = requisicaoService.find(requisicao_id);
        FormularioDiariasTransportePdf generator = new FormularioDiariasTransportePdf();
        ByteArrayInputStream bis = generator.gerar(req);

        String filename = String.format(
                "inline; filename=\"%d-Formulario-Diarias-Transporte.pdf\"",
                requisicao_id
        );
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @GetMapping(value = "/{requisicao_id}/relatorio_viagem", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> ralatorioViagemProposto(
            @PathVariable("requisicao_id") Long requisicao_id) throws IOException {

        Requisicao requisicao = this.requisicaoService.find(requisicao_id);
        RelatorioViagemRequisicaoPropostoPdf pdf = new RelatorioViagemRequisicaoPropostoPdf();
        RelatorioViagemRequisicaoProposto relatorioViagemProposto;

        if (requisicao.hasRealtorioViagemProposto()) {

            relatorioViagemProposto = requisicao.getRelatorioViagemProposto();

        } else {

            Long rand = (long) (Math.random() * 13);
            Long rand2 = (long) (Math.random() * 8);
            Long codigoVerificacao = Long.parseLong(rand2 + requisicao.getId().toString() + rand);

            relatorioViagemProposto = new RelatorioViagemRequisicaoProposto();

            relatorioViagemProposto.setRequisicao(requisicao);
            relatorioViagemProposto.setCodigoVerificacao(codigoVerificacao);
            relatorioViagemProposto.setDataEmissao(LocalDate.now());
            relatorioViagemProposto
                    .setIdentificador(ValidacaoDocumentoController.DOC_RELATORIO_VIAGEM_REQUISICAO_PROPOSTO);
            relatorioViagemProposto.setRequisicao(requisicao);

            relatorioViagemProposto = this.relatorioViagemPropostoRepository.save(relatorioViagemProposto);

            requisicao.setRelatorioViagemProposto(relatorioViagemProposto);

            requisicao = this.requisicaoService.save(requisicao);
            relatorioViagemProposto = requisicao.getRelatorioViagemProposto();
        }

        ByteArrayInputStream termoPdf = pdf.gerar(relatorioViagemProposto);

        final String filename = "inline; filename=\"" + requisicao_id + "-RELATORIO VIAGEM PROPOSTO.pdf\"";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", filename);

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(termoPdf));
    }

    @GetMapping(value = "/{viagem_id}/gerar_relatorio", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> gerarRelatorioViagem(@PathVariable("viagem_id") Long viagem_id)
            throws FileNotFoundException {

        Viagem viagem = this.viagemService.find(viagem_id);
        ViagemRelatorio viagemRelatorio = new ViagemRelatorio();

        viagemRelatorio.setIdentificador(1);
        viagemRelatorio.setDataEmissao(LocalDate.now());
        viagemRelatorio.setViagem(viagem);

        viagemRelatorio = this.viagemRelatorioService.save(viagemRelatorio);
        Long rand = (long) (Math.random() * 10);
        Long codigoVerificacao = Long.parseLong(viagemRelatorio.getId().toString() + viagem.getId().toString() + rand);

        viagemRelatorio.setCodigoVerificacao(codigoVerificacao);
        viagemRelatorio = this.viagemRelatorioService.update(viagemRelatorio);

        ByteArrayInputStream bis = this.relatorioViagemPdf.gerar(viagemRelatorio);// RelatorioViagem.citiesReport(viagemRelatorio);
        InputStreamResource body = new InputStreamResource(bis);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity.ok().headers(headers).body(body);
    }

    @GetMapping("/{requisicao_id}/justificativa_pedido_fora_prazo_legal")
    public ResponseEntity<InputStreamResource> gerarJustificativaPedidoForaPrazoLegal(
            @PathVariable("requisicao_id") Long requisicao_id) {

        ByteArrayInputStream bis = RelatorioViagem.justificativa(justificativaService.findBy(requisicao_id));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=justificativa_fora_do_prazo_legal.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    private String findMimeType(Resource resource) {
        try {

            Metadata metadata = new Metadata();
            BodyContentHandler bodyContentHandler = new BodyContentHandler();
            ParseContext context = new ParseContext();
            metadata.set(TikaCoreProperties.RESOURCE_NAME_KEY, resource.getFilename());

            Parser parser = new AutoDetectParser();
            parser.parse(resource.getInputStream(), bodyContentHandler, metadata, context);

            return metadata.get(Metadata.CONTENT_TYPE);

        } catch (IOException | SAXException | TikaException ex) {
            Logger.getLogger(AnexoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String getFileExtension(String filename) {

        int index = filename.lastIndexOf(".");

        if (index > 0) {

            return filename.substring(index);
        }

        return "";
    }
}
