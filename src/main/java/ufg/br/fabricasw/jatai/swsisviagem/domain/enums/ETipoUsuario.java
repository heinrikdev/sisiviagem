package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 * Created by guilherme on 10/10/17.
 */
public enum ETipoUsuario {
    
    Proponente {
        
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Proponente");
        }

    },
    
    Desacordo {
        
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Desacordo");
        }

    }, 
    
    Motorista {
        
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Motorista");
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();

}
