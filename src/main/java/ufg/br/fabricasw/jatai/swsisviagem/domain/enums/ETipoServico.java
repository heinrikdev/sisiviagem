package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum ETipoServico {
    Contrato {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Contrato");
        }

    },
    Tickt_Car {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Tickt_Car");
        }

    },
    Interno {
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Interno");
        }

    },
    Outros {
        @Override
        public Integer id() {
            return (3);
        }

        @Override
        public String toString() {
            return ("Outros");
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
