package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum EEstadoTarefa {
    Concluida {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Concluída");
        }

    },
    NaoRealizada {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Não Realizada");
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

    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
