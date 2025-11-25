package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum ETipoSolicitacao {

    DIARIA {
        @Override
        public Integer id() {
            return 0;
        }

        @Override
        public String toString() {
            return "Diária";
        }
    },
    TRANSPORTE {
        @Override
        public Integer id() {
            return 1;
        }

        @Override
        public String toString() {
            return "Transporte Oficial";
        }
    },
    PASSAGEM {
        @Override
        public Integer id() {
            return 2;
        }

        @Override
        public String toString() {
            return "Passagem";
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();

}
