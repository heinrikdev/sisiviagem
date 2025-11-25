package ufg.br.fabricasw.jatai.swsisviagem.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Arquivo não existe ou não foi possível lê-lo")
public class StorageFileNotFoundException extends StorageException {

    public StorageFileNotFoundException(String message) {
        
        super(message);
        
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
       
        super(message, cause);
        
    }
}
