package org.sgc.rak.core;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * Configuration for the application.
 */
@Configuration
@ComponentScan("org.sgc.rak")
@EnableSpringDataWebSupport
public class AppConfiguration {

    @Bean
    public ActivityProfileDao activityProfileDao() {
        return new ActivityProfileDao();
    }

    @Bean
    public CompoundDao compoundDao() {
        return new CompoundDao();
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
