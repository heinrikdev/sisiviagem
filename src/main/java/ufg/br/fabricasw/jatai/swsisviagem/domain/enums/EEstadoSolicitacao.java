package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/** 
 *
 * Created by thevilela on 18/03/17.
 */
public enum EEstadoSolicitacao {

    APROVADA {
        @Override
        public Integer id() {
            return 0;
        }

        @Override
        public String toString() {
            return "Aprovada";
        }
    },
    RECUSADA {
        @Override
        public Integer id() {
            return 1;
        }

        @Override
        public String toString() {
            return "Recusada";
        }
    },
    EM_AVALIACAO {
        @Override
        public Integer id() {
            return 2;
        }

        @Override
        public String toString() {
            return "Em avaliação";
        }
    },
    PREENCHENDO {
        @Override
        public Integer id() {
            return 3;
        }

        @Override
        public String toString() {
            return "Preenchendo...";
        }
    },
    
    PREENCHIDA {
        @Override
        public Integer id() {
            return 4;
        }

        @Override
        public String toString() {
            return "Preenchida";
        }
    },
    SUBMETIDA {
        @Override
        public Integer id() {
            return 5;
        }

        @Override
        public String toString() {
            return "Submetida";
        }
    },
    ENCERRADA {
        @Override
        public Integer id() {
            return 6;
        }

        @Override
        public String toString() {
            return "Encerrada";
        }
    },
    DEVOLVIDA {
        @Override
        public Integer id() {
            return 7;
        }

        @Override
        public String toString() {
            return "Devolvida para revisão";
        }
    },
    /**
     * Este enum é utilizado somente para filtrar as requisições,
     * neste caso, busca as requisições idependente do seu estado.
     */
    FILTRO_BUSCAR_TODOS {
        @Override
        public Integer id() {
            return 8;
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
