package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum EEstadoPassageiro {
    Presente {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Presente");
        }

    },
    Ausente {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Ausente");
        }

    },
    Aguardando {
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Aguardando");
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
