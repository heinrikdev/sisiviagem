package ufg.br.fabricasw.jatai.swsisviagem.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ufg.br.fabricasw.jatai.swsisviagem.service.UsuarioService;

/**
 * Created by guilherme on 06/09/17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioService usuarioService;

    public WebSecurityConfig() {
        super();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .headers()
                .httpStrictTransportSecurity()
                .maxAgeInSeconds(0)
                .includeSubDomains(true);

        http
            .authorizeRequests()
                .antMatchers("/app/**", "/dashboard/**")
                .authenticated()
                .and()
            .authorizeRequests()
                .antMatchers("/", "/**", "/login", "/confirmarDados", "/documentos", "/documentos/**", "/teste")
                .permitAll().anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .successForwardUrl("/dashboard")
                .permitAll()
                .and()
            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService);
    }


}
