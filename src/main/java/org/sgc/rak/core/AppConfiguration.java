package org.sgc.rak.core;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.repositories.KinaseActivityProfileRepository;
import org.sgc.rak.rest.CsvHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration for the application.
 */
@Configuration
@ComponentScan("org.sgc.rak")
@EnableSpringDataWebSupport
public class AppConfiguration implements WebMvcConfigurer {

    @Bean
    public ActivityProfileDao activityProfileDao(KinaseActivityProfileRepository activityProfileRepository) {
        return new ActivityProfileDao(activityProfileRepository);
    }

    @Bean
    public CompoundDao compoundDao() {
        return new CompoundDao();
    }

    /**
     * Add CSV support.
     *
     * @param configurer The content negotiation configurer.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.mediaType("csv", new MediaType("text","csv"));
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(CsvHttpMessageConverter.csv(new CsvMapper()));
    }

    @Bean
    public KinaseDao kinaseDao() {
        return new KinaseDao();
    }

    @Bean
    public Messages messages() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("org.sgc.rak.i18n.Messages");
        return new Messages(source);
    }
}
