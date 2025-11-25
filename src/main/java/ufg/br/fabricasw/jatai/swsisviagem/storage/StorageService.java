package ufg.br.fabricasw.jatai.swsisviagem.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import ufg.br.fabricasw.jatai.swsisviagem.domain.arquivo.Anexo;

public interface StorageService {

    void init();

    void store(MultipartFile file);
    
    void store(List<Anexo> anexos);
    
    void store(Anexo anexo);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

    void deletar(Anexo anexo);
    
    void deletar(List<Anexo> anexo);
}
