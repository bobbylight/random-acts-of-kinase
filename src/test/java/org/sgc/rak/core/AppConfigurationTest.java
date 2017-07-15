package org.sgc.rak.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sgc.rak.i18n.Messages;

import java.util.Locale;

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
        Assert.assertNotNull(config.activityProfileDao());
    }

    @Test
    public void testCompoundDao() {
        Assert.assertNotNull(config.compoundDao());
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
