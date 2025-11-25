package ufg.br.fabricasw.jatai.swsisviagem.service;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import ufg.br.fabricasw.jatai.swsisviagem.domain.Justificativa;
import ufg.br.fabricasw.jatai.swsisviagem.repository.AbstractCrudService;
import ufg.br.fabricasw.jatai.swsisviagem.repository.JustificativaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import org.springframework.core.io.InputStreamResource;
import ufg.br.fabricasw.jatai.swsisviagem.componente.RelatorioViagem;
import ufg.br.fabricasw.jatai.swsisviagem.domain.requisicao.Requisicao;

@Service
public class JustificativaService implements AbstractCrudService<Justificativa, Long> {

    @Autowired
    JustificativaRepository repository;

    @Autowired
    private RequisicaoServiceImpl requisicaoServiceImpl;

    public Justificativa findBy(Long requisicao_id) {
        return repository.findByRequisicao(requisicao_id);
    }

    @Override
    public Justificativa save(Justificativa entity) {
        return repository.save(entity);
    }

    @Override
    public Justificativa update(Justificativa entity) {
        return repository.save(entity);
    }

    @Override
    public Justificativa findOne(Long integer) {
        return repository.getOne(integer);
    }

    @Override
    public boolean exists(Long integer) {
        return repository.existsById(integer);
    }

    @Override
    public List<Justificativa> findAll() {
        return (List<Justificativa>) repository.findAll();
    }

    @Override
    public void delete(Long integer) {
        repository.deleteById(integer);
    }

    @Override
    public void delete(Justificativa entity) {
        repository.delete(entity);
    }

    public Justificativa findBycodigoVerificacao(Long codigo) {
        return repository.findBycodigoVerificacao(codigo);
    }

    public InputStreamResource gerarJustificativaPedidoForaPrazoLegal(Long requisicao_id, String motivo,
            String responsavel) {

        Requisicao requisicao = this.requisicaoServiceImpl.find(requisicao_id);
        Justificativa justificativa = new Justificativa();

        justificativa.setRequisicao(requisicao);
        justificativa.setIdentificador(2);
        justificativa.setPessoa(responsavel);
        justificativa.setMotivo(motivo);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        justificativa.setDataEmissao(sdf.format(new Date()));

        Locale BRAZIL = new Locale("pt", "BR");
        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, BRAZIL);

        justificativa.setDataFull(df.format(new Date()));
        justificativa = this.save(justificativa);
        justificativa.setCodigoVerificacao(Long.parseLong(justificativa.getId().toString() + requisicao_id.toString()));

        ByteArrayInputStream bis = RelatorioViagem.justificativa(this.update(justificativa));

        return new InputStreamResource(bis);
    }
}
