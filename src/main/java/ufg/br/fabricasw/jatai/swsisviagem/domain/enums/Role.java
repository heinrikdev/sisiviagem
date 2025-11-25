package ufg.br.fabricasw.jatai.swsisviagem.domain.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by guilherme on 09/09/17.
 */
public enum Role implements GrantedAuthority {
    USER {
        @Override
        public String getAuthority() {
            return Role.USER.toString();
        }

    }, TRANSPORTE {
        @Override
        public String getAuthority() {
            return Role.TRANSPORTE.toString();
        }

    }, ADMIN {
        @Override
        public String getAuthority() {
            return Role.ADMIN.toString();
        }

    }, DIARIA {
        @Override
        public String getAuthority() {
            return Role.DIARIA.toString();
        }
    }, PASSAGEM {
        @Override
        public String getAuthority() {
            return Role.PASSAGEM.toString();
        }
    }, MOTORISTA {
        @Override
        public String getAuthority() {
            return Role.MOTORISTA.toString();
        }
    }, AUDITOR {
        @Override
        public String getAuthority() {
            return Role.AUDITOR.toString();
        }
    };

    @Override
    public abstract String getAuthority();
}
