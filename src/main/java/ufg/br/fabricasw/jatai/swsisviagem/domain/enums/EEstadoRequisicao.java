package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 * Created by Carlos
 */
public enum EEstadoRequisicao {

    PREENCHENDO {
        @Override
        public Integer id() {
            return 0;
        }

        @Override
        public String toString() {
            return "Preenchendo requisição...";
        }
    },
    PREENCHIDA {
        @Override
        public Integer id() {
            return 1;
        }

        @Override
        public String toString() {
            return "Requisição preenchida";
        }
    },
    SUBMETIDA {
        @Override
        public Integer id() {
            return 2;
        }

        @Override
        public String toString() {
            return "Requisição submetida";
        }
    },
    DEVOLVIDA {
        @Override
        public Integer id() {
            return 3;
        }

        @Override
        public String toString() {
            return "Devolvida para revisão";
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
