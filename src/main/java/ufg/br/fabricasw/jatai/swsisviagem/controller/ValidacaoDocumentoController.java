package ufg.br.fabricasw.jatai.swsisviagem.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ufg.br.fabricasw.jatai.swsisviagem.componente.RelatorioViagem;
import ufg.br.fabricasw.jatai.swsisviagem.componente.relatorio_jasper.RelatorioViagemPdf;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Justificativa;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.RelatorioViagemRequisicaoProposto;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.TermoOpcaoCompromissoResponsabilidade;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ComprovanteViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.viagem.ViagemRelatorio;
import ufg.br.fabricasw.jatai.swsisviagem.repository.RelatorioViagemRequisicaoPropostoRepository;
import ufg.br.fabricasw.jatai.swsisviagem.repository.TermoOpcaoCompromisoResponsabilidadeRepository;
import ufg.br.fabricasw.jatai.swsisviagem.service.ComprovanteViagemService;
import ufg.br.fabricasw.jatai.swsisviagem.service.JustificativaService;
import ufg.br.fabricasw.jatai.swsisviagem.service.ViagemRelatorioService;

/**
 *
 * @author Ronaldo N Sousa
 *         Criado em: 07/06/2019
 */
@Controller
public class ValidacaoDocumentoController {

    public static int DOC_TERMO_COMPROMISSO_RESPONSABILIDADE = 4;

    public static int DOC_RELATORIO_VIAGEM_REQUISICAO_PROPOSTO = 5;

    @Autowired
    private ViagemRelatorioService viagemRelatorioService;

    @Autowired
    private JustificativaService justificativaService;

    @Autowired
    private RelatorioViagemPdf relatorioViagemPdf;

    @Autowired
    private TermoOpcaoCompromisoResponsabilidadeRepository termoResponsabilidadeRepository;

    @Autowired
    private RelatorioViagemRequisicaoPropostoRepository relatorioViagemPropostoRepository;

    @Autowired
    private ComprovanteViagemService comprovanteViagemService;

    @GetMapping("/documentos")
    public String documentos(@RequestParam(value = "okay", required = false) String okay,
            Model model) {

        if (okay != null) {

            model.addAttribute("error", "Documento não existe, por favor verifique "
                    + "se os dados estão corretos");
        }

        return "app/validacao_documentos/validacao_form";
    }

    @RequestMapping(value = "documentos/validar", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public String validar(@RequestParam("identificador") Integer identificador,
            @RequestParam("dataDocumento") LocalDate dataDocumento,
            @RequestParam("codigoVerificacao") Long codigoVerificacao) throws IOException {

        if (identificador == 1) {

            ViagemRelatorio fileVerificacao = viagemRelatorioService.findBycodigoVerificacao(codigoVerificacao);

            if (fileVerificacao.getDataEmissao().equals(dataDocumento) && fileVerificacao.getIdentificador() == 1) {

                return "redirect:/documentos/relatorio-viagem/" + fileVerificacao.getId();
            }

        } else if (identificador == 2) {

            Justificativa fileVerificacao = justificativaService.findBycodigoVerificacao(codigoVerificacao);

            if (fileVerificacao.getDataEmissao().equals(dataDocumento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    && fileVerificacao.getIdentificador() == 2) {

                return "redirect:/documentos/justificativa/" + fileVerificacao.getId();
            }

        } else if (identificador == 3) {

            ComprovanteViagem fileVerificacao = comprovanteViagemService.findByCodVerificacao(codigoVerificacao);

            if (fileVerificacao.getDataEmissao().equals(dataDocumento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    && fileVerificacao.getIdentificador() == 3) {

                return "redirect:/documentos/comprovante-viagem/" + fileVerificacao.getId();
            }
        } else if (identificador == DOC_RELATORIO_VIAGEM_REQUISICAO_PROPOSTO) {

            RelatorioViagemRequisicaoProposto relatorioViagemProposto = this.relatorioViagemPropostoRepository
                    .findByCodigoVerificacaoAndDataEmissaoAndIdentificador(codigoVerificacao, dataDocumento,
                            identificador);

            if (relatorioViagemProposto != null) {

                return "redirect:/dashboard/anexo/" + relatorioViagemProposto.getRequisicao().getId()
                        + "/relatorio_viagem";
            }
        } else if (identificador == DOC_TERMO_COMPROMISSO_RESPONSABILIDADE) {

            TermoOpcaoCompromissoResponsabilidade termoResponsabilidade = this.termoResponsabilidadeRepository
                    .findByCodigoVerificacaoAndDataEmissaoAndIdentificador(codigoVerificacao, dataDocumento,
                            identificador);

            if (termoResponsabilidade != null) {

                Long id_requisicao = termoResponsabilidade.getVeiculoProprio().getRequisicao().getId();

                return "redirect:/dashboard/anexo/" + id_requisicao + "/documento_veiculo_proprio";
            }
        }

        return "redirect:/documentos?okay=false";

    }

    @RequestMapping(value = "/documentos/relatorio-viagem/{idFile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> chamadaReport(@PathVariable("idFile") Long idFile)
            throws IOException {

        ViagemRelatorio viagemRelatorio = viagemRelatorioService.findOne(idFile);
        ByteArrayInputStream bis = this.relatorioViagemPdf.gerar(viagemRelatorio);// RelatorioViagem.citiesReport(viagemRelatorio);
                                                                                  // //

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/documentos/justificativa/{idFile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> justificativa(@PathVariable("idFile") Long idFile)
            throws IOException {
        ByteArrayInputStream bis = RelatorioViagem.justificativa(justificativaService.findOne(idFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

    @RequestMapping(value = "/documentos/comprovante-viagem/{idFile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> comprovante(@PathVariable("idFile") Long idFile)
            throws IOException {
        ByteArrayInputStream bis = RelatorioViagem.comprovanteDerViagem(comprovanteViagemService.findOne(idFile));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=viagem.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
