package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

/**
 * Created by guilherme on 10/10/17.
 */
public enum EUnidadeDepartamento {

    UnidadeAcademica {
        @Override
        public Integer id() {
            return (0);
        }

        @Override
        public String toString() {
            return ("Unidade Acadêmica");
        }

    },
    UnidadeAdministrativa {
        @Override
        public Integer id() {
            return (1);
        }

        @Override
        public String toString() {
            return ("Unidade Administrativa");
        }

    };

    public abstract Integer id();

    @Override
    public abstract String toString();
}
