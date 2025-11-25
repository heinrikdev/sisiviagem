package ufg.br.fabricasw.jatai.swsisviagem.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ufg.br.fabricasw.jatai.swsisviagem.domain.util.data_form_binder.AnnotationServletModelAttributeResolver;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.DateFormaFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.DoubleFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.IntegerFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.domain.UnidadeDepartamentoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.EEstadoSolicitacaoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.EEstadoViagemFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.EInfoTrajetoPassageiroFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.ETipoCombustivelFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.ETipoRequisicaoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.ETipoServicoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.EUnidadeDepartamentoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.domain.SolicitacaoFormatter;
import ufg.br.fabricasw.jatai.swsisviagem.service.formatters.enums.EEstadoPassageiroFormatter;

/**
 *
 * @author Ronaldo N. de Sousa
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    
    /**
     * Locais de recursos como css, js, imagens etc..
     */
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
        "classpath:/static/"
    };

    @Autowired
    private EUnidadeDepartamentoFormatter eunidadeDepartamentoFormatter;
    @Autowired
    private UnidadeDepartamentoFormatter unidadeDepartamentoFormatter;
    @Autowired
    private ETipoRequisicaoFormatter eTipoRequisicaoFormatter;
    @Autowired
    private SolicitacaoFormatter solicitacaoFormatter;
    @Autowired
    private DateFormaFormatter dateFormaFormatter;
    @Autowired
    private EInfoTrajetoPassageiroFormatter infoTrajetoPassageiroFormatter;
    @Autowired
    private EEstadoSolicitacaoFormatter eEstadoSolicitacaoFormatter;
    @Autowired
    private ETipoCombustivelFormatter eTipoCombustivelFormatter;
    @Autowired
    private ETipoServicoFormatter eTipoServicoFormatter;
    @Autowired
    private EEstadoViagemFormatter eEstadoViagemFormatter;
    @Autowired
    private EEstadoPassageiroFormatter eEstadoPassageiroFormatter;
    @Autowired
    private DoubleFormatter doubleFormatter;
    @Autowired
    private IntegerFormatter integerFormatter;
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
        //.setCacheControl(CacheControl.maxAge(45, TimeUnit.MINUTES));
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AnnotationServletModelAttributeResolver(false));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addFormatter(this.eunidadeDepartamentoFormatter);
        registry.addFormatter(this.unidadeDepartamentoFormatter);
        registry.addFormatter(this.solicitacaoFormatter);
        registry.addFormatter(this.eTipoRequisicaoFormatter);
        registry.addFormatter(this.dateFormaFormatter);
        registry.addFormatter(this.infoTrajetoPassageiroFormatter);
        registry.addFormatter(this.eEstadoSolicitacaoFormatter);
        registry.addFormatter(this.eTipoCombustivelFormatter);
        registry.addFormatter(this.eTipoServicoFormatter);
        registry.addFormatter(this.eEstadoViagemFormatter);
        registry.addFormatter(this.eEstadoPassageiroFormatter);
        registry.addFormatter(this.doubleFormatter);
        registry.addFormatter(this.integerFormatter);
    }
    
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        converters.stream().filter((converter) -> (converter instanceof MappingJackson2HttpMessageConverter)).forEachOrdered((converter) -> {
            
            Hibernate5Module module = new Hibernate5Module();
            
            module.disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);
            module.enable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
            
            ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
            mapper.registerModule(module);
        });
    }
}
