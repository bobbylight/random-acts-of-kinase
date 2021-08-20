package org.sgc.rak.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.repositories.NanoBretActivityProfileRepository;
import org.sgc.rak.rest.CsvHttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AppConfigurationTest {

    private AppConfiguration config;
    private Locale origLocale;

    @BeforeEach
    public void setUp() {
        config = new AppConfiguration();
        origLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    public void tearDown() {
        Locale.setDefault(origLocale);
    }

    @Test
    public void testActivityProfileDao() {
        Assertions.assertNotNull(config.activityProfileDao(null));
    }

    @Test
    public void testCompoundDao() {
        Assertions.assertNotNull(config.compoundDao());
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
        Assertions.assertEquals(1, converters.size());
        Assertions.assertTrue(converters.get(0) instanceof CsvHttpMessageConverter);
    }

    @Test
    public void testImageTranscoder() {
        Assertions.assertNotNull(config.imageTranscoder());
    }

    @Test
    public void testKinaseDao() {
        Assertions.assertNotNull(config.kinaseDao());
    }

    @Test
    public void testMessages() {
        Messages messages = config.messages();
        String result = messages.get("error.noSuchCompound", "foo");
        Assertions.assertEquals("No such compound: foo", result);
    }

    @Test
    public void testNanoBretActivityProfileDao() {
        NanoBretActivityProfileRepository mockRepository = Mockito.mock(NanoBretActivityProfileRepository.class);
        Assertions.assertNotNull(config.nanoBretActivityProfileDao(mockRepository));
    }
}
