package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 *
 * @author fsw
 */
public enum ETipoQuilometragem {
    
    KM_INICIAL {
        @Override
        public String toString() {
            return ("km inicial");
        }
    },
    KM_FINAL {
        @Override
        public String toString() {
            return ("km final");
        }
    }
}
