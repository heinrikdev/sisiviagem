package ufg.br.fabricasw.jatai.swsisviagem.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private static final Logger LOG = LoggerFactory.getLogger(StorageProperties.class.getName());
    private static final String LOCATION = "/upload/";

    public String getLocation() {
        LOG.info("Localização: {}", LOCATION);
        return LOCATION;
    }
}
