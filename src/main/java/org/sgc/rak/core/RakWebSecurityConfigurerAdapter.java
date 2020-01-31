package org.sgc.rak.core;

import org.apache.commons.lang3.StringUtils;
import org.sgc.rak.services.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * App application configuration related to security.
 */
@Configuration
// https://stackoverflow.com/questions/49108156/spring-boot-2-and-migrating-oauth2-configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2) //ACCESS_OVERRIDE_ORDER)
public class RakWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    private final Environment environment;
    private final AuditService auditService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RakWebSecurityConfigurerAdapter.class);

    @Autowired
    public RakWebSecurityConfigurerAdapter(Environment environment, AuditService auditService) {
        this.environment = environment;
        this.auditService = auditService;
    }

    private AccessDeniedHandler accessDeniedHandler(AuditService auditService) {
        return new RakAccessDeniedHandler(auditService);
    }

    /**
     * For now we only have a single admin account.  In the future we may need to migrate to JDBC-based
     * authentication.
     *
     * @param auth The authentication manager builder.
     * @throws Exception If an error occurs.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        String user = this.environment.getProperty("rak.login.user");
        String password = this.environment.getProperty("rak.login.password");

        if (StringUtils.isNotBlank(user) && StringUtils.isNotBlank(password)) {

            password = "{bcrypt}" + new BCryptPasswordEncoder().encode(password);

            auth.inMemoryAuthentication()
                .withUser(user).password(password)
                    .authorities("ROLE_USER", "ROLE_ADMIN");
            LOGGER.info("User was configured");
        }
        else {
            LOGGER.warn("No users were configured.  Logging in will not be possible");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .httpBasic()
            .and()
                .authorizeRequests()
                    .anyRequest().permitAll()
            .and()
                .csrf()
                    // Annoyingly, we must itemize POST calls made by bots that we want to return 404's, since
                    // Spring Security will return 403 for any POST without a CSRF token otherwise
                    .ignoringRequestMatchers(new RegexRequestMatcher(".+\\.php", "POST"))
                    // CookieCsrfTokenRepository uses the default cookie & header names as axios
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler(auditService))
                .permitAll()
            .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler(auditService));
        // @formatter:on
    }
}
