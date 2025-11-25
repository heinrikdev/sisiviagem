package ufg.br.fabricasw.jatai.swsisviagem.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@Configuration
@EnableLdapRepositories(basePackages = "ufg.br.fabricasw.jatai.swsisviagem.repository.ldap")
public class LdapConnectionConfig {}
