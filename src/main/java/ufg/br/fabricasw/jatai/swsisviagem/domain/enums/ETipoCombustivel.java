package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

public enum ETipoCombustivel {
    Gasolina {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Gasolina");
        }

    },
    Etanol {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Etanol");
        }

    },
    Diesel {
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Diesel");
        }

    },
    Diesel_S10 {
        @Override
        public Integer id() {
            return 3;
        }

        @Override
        public String toString() {
            return "Diesel_S10";
        }
    },
    ARLA_32 {
        @Override
        public Integer id() {
            return 4;
        }

        @Override
        public String toString() {
            return "ARLA_32";
        }

    },
    TROCA_DE_OLEO {
        @Override
        public Integer id() {
            return 5;
        }

        @Override
        public String toString() {
            return "TROCA_DE_OLEO";
        }

    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
