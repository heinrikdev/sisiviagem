package ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Post-processor to be used if any modifications to the handler adapter need to be made
 * 
 * Solução encontrada em: https://stackoverflow.com/questions/8986593/how-to-customize-parameter-names-when-binding-spring-mvc-command-objects
 * @author bozho
 *
 */
public class AnnotationHandlerMappingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String arg1) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String arg1) throws BeansException {
        
        if (bean instanceof RequestMappingHandlerAdapter) {
            
            RequestMappingHandlerAdapter adapter = (RequestMappingHandlerAdapter) bean;
            List<HandlerMethodArgumentResolver> resolvers = adapter.getCustomArgumentResolvers();
            
            if (resolvers == null) {
                
                resolvers = new ArrayList<>();
            }
            
            resolvers.add(new AnnotationServletModelAttributeResolver(false));
            adapter.setCustomArgumentResolvers(resolvers);
        }

        return bean;
    }
}
