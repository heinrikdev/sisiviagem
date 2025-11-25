package ufg.br.fabricasw.jatai.swsisviagem.componente;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Component
public class SessionEndedListener implements ApplicationListener<SessionDestroyedEvent> {

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        
        System.out.println("ENDED");
        
        for (SecurityContext securityContext : event.getSecurityContexts())
        {
            Authentication auth = securityContext.getAuthentication();
            System.out.println("U: " + auth.getName());
        }
    }
}