package ufg.br.fabricasw.jatai.swsisviagem;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
// import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.AnnotationHandlerMappingPostProcessor;
import ufg.br.fabricasw.jatai.swsisviagem.storage.StorageProperties;

@SpringBootApplication
public class SwSisViagemApplication {

    // @Value("${ajp.port}")
    // int ajpPort;

    // @Value("${ajp.enabled}")
    // boolean ajpEnabled;

    public static void main(String[] args) {
        SpringApplication.run(SwSisViagemApplication.class, args);
    }

    @PostConstruct
    void started() {

        Locale.setDefault(new Locale("pt", "BR"));
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
        // adiciona o NoOp para aceitar {noop}
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder delegating = new DelegatingPasswordEncoder(idForEncode, encoders);
        // fallback para MD5 caso venha sem prefixo
        delegating.setDefaultPasswordEncoderForMatches(new MessageDigestPasswordEncoder("MD5"));
        return delegating;
    }

    @Bean
    @Primary
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    // @Bean
    // public BeanPostProcessor beanPostProcessor() {
    //     return new AnnotationHandlerMappingPostProcessor();
    // }

    // @Bean
    // public Java8TimeDialect java8TimeDialect() {
    //     return new Java8TimeDialect();
    // }

    // @Bean
    // public ServletWebServerFactory servletContainer() {

    //     if (ajpEnabled) {
    //         return ajap();
    //     }

    //     return new TomcatServletWebServerFactory();
    // }

    // private ServletWebServerFactory ajap() {

    //     TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

    //     Connector ajpConnector = new Connector("AJP/1.3");
    //     // ajpConnector.setPort(ajpPort);
    //     ajpConnector.setSecure(false);
    //     ajpConnector.setScheme("http");
    //     ajpConnector.setAllowTrace(false);
    //     tomcat.addAdditionalTomcatConnectors(ajpConnector);

    //     return tomcat;
    // }

}
