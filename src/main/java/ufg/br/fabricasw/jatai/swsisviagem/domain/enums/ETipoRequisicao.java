package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 * Created by guilherme on 10/10/17.
 */
public enum ETipoRequisicao {
    Administrativa {
        @Override
        public String toString() {
            return ("Administrativa");
        }

        public Integer id() {
            return (0);
        }
    },
    Graduacao {
        @Override
        public String toString() {
            return ("Graduacao");
        }

        public Integer id() {
            return (1);
        }
    },
    Pos_Graduacao {
        @Override
        public String toString() {
            return ("Pos_Graduacao");
        }

        public Integer id() {
            return (2);
        }
    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
