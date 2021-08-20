package org.sgc.rak.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.sgc.rak.services.AuditService;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import static org.mockito.Mockito.doReturn;

public class RakWebSecurityConfigurationAdapterTest {

    @Mock
    private Environment mockEnvironment;

    @InjectMocks
    private RakWebSecurityConfigurerAdapter adapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConfigureGlobal_passwordBlank() throws Exception {

        doReturn("0ceans11").when(mockEnvironment).getProperty("rak.login.user");

        AuthenticationManagerBuilder mockAuth = Mockito.mock(AuthenticationManagerBuilder.class);
        adapter.configureGlobal(mockAuth);
    }

    @Test
    public void testConfigureGlobal_userBlank() throws Exception {

        doReturn("0ceans11").when(mockEnvironment).getProperty("rak.login.password");

        AuthenticationManagerBuilder mockAuth = Mockito.mock(AuthenticationManagerBuilder.class);
        adapter.configureGlobal(mockAuth);
    }

    @Test
    public void testConfigureGlobal_userAndPasswordBlank() throws Exception {
        AuthenticationManagerBuilder mockAuth = Mockito.mock(AuthenticationManagerBuilder.class);
        adapter.configureGlobal(mockAuth);
    }

    @Test
    @Disabled("Not sure yet how to mock authentication")
    public void testConfigureGlobal_success() {
    }
}
