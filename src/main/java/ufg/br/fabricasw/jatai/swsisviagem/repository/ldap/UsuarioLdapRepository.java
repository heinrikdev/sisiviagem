package ufg.br.fabricasw.jatai.swsisviagem.repository.ldap;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;
import ufg.br.fabricasw.jatai.swsisviagem.domain.ldap.UsuarioLdap;

import java.util.Optional;

@Repository
public interface UsuarioLdapRepository extends LdapRepository<UsuarioLdap> {

    Optional<UsuarioLdap> findByLoginUnicoAndPassword(String loginUnico, String password);

}
