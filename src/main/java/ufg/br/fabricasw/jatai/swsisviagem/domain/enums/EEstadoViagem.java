package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum EEstadoViagem {
    Encerrada {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Encerrada");
        }

    },
    Finalizada {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Finalizada");
        }
    },
    Aberta {
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Aberta");
        }
    },
    Em_andamento {
        @Override
        public Integer id() {
            return (3);
        }

        @Override
        public String toString() {
            return ("Em andamento");
        }

    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
