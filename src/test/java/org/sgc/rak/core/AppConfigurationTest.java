package org.sgc.rak.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.rest.CsvHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AppConfigurationTest {

    private AppConfiguration config;
    private Locale origLocale;

    @Before
    public void setUp() {
        config = new AppConfiguration();
        origLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @After
    public void tearDown() {
        Locale.setDefault(origLocale);
    }

    @Test
    public void testActivityProfileDao() {
        Assert.assertNotNull(config.activityProfileDao(null));
    }

    @Test
    public void testCompoundDao() {
        Assert.assertNotNull(config.compoundDao());
    }

    @Test
    public void testConfigureContentNegotiation() {

        ContentNegotiationConfigurer configurer = Mockito.mock(ContentNegotiationConfigurer.class);

        config.configureContentNegotiation(configurer);
        verify(configurer, times(1)).mediaType(eq("csv"), any(MediaType.class));
    }

    @Test
    public void testExtendMessageConverters() {

        List<HttpMessageConverter<?>> converters = new ArrayList<>();

        config.extendMessageConverters(converters);
        Assert.assertEquals(1, converters.size());
        Assert.assertTrue(converters.get(0) instanceof CsvHttpMessageConverter);
    }

    @Test
    public void testKinaseDao() {
        Assert.assertNotNull(config.kinaseDao());
    }

    @Test
    public void testMessages() {
        Messages messages = config.messages();
        String result = messages.get("error.noSuchCompound", "foo");
        Assert.assertEquals("No such compound: foo", result);
    }
}
