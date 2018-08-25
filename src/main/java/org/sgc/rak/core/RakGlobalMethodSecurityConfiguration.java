package org.sgc.rak.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Enable Spring's per-method security configuration.  Enables use of {@code @RolesAlloed()} to control
 * who can call what controller endpoints.
 */
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class RakGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
}
