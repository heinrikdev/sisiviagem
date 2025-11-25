package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 * Este ENUM representa o tipo de trajeto do usuário, ou seja, se ele só vai ou
 * só volta, ou se ambos.
 * @author ronogue
 */
public enum EInfoTrajetoPassageiro {
    Ida {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Ida");
        }

    },
    Volta {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Volta");
        }

    },
    IdaEVolta {
        @Override
        public Integer id() {
            return (2);
        }

        @Override
        public String toString() {
            return ("Ida e Volta");
        }
    },
    NaoInformado {

        @Override
        public Integer id() {
            return (3);
        }

        @Override
        public String toString() {
            return ("Não informado");
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
