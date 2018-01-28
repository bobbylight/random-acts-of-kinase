package org.sgc.rak.core;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * App application configuration related to security.
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] NO_SECURITY_RESOURCES = {
        "/",
        "/index.html",
        "/*.js",
        "/*.eot",
        "/*.svg",
        "/*.ttf",
        "/*.woff",
        "/*.woff1",
        "/img/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .httpBasic().and()
            .authorizeRequests()
//                .antMatchers(HttpMethod.GET, "/login").authenticated()
                .anyRequest().permitAll()
//                .antMatchers(NO_SECURITY_RESOURCES).permitAll()
//                .anyRequest().authenticated()
                .and()
            .csrf()
                // CookieCsrfTokenRepository uses the default cookie & header names as axios
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }
}
