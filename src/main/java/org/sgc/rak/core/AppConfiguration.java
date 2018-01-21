package org.sgc.rak.core;

import org.sgc.rak.dao.ActivityProfileDao;
import org.sgc.rak.dao.CompoundDao;
import org.sgc.rak.dao.KinaseDao;
import org.sgc.rak.i18n.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configuration for the application.
 */
@Configuration
@ComponentScan("org.sgc.rak")
@EnableSpringDataWebSupport
@EnableWebSecurity
public class AppConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public ActivityProfileDao activityProfileDao() {
        return new ActivityProfileDao();
    }

    @Bean
    public CompoundDao compoundDao() {
        return new CompoundDao();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//            .antMatchers("/api/compounds/*").permitAll()
//            .anyRequest().authenticated()
//            .and()
//            .formLogin()
//            .loginPage("/login")
//            .permitAll()
//            .and()
//            .logout()
//            .permitAll();
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//            .inMemoryAuthentication()
//            .withUser("user").password("password").roles("USER");
//    }

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
