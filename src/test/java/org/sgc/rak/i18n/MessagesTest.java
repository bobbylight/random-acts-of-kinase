package org.sgc.rak.i18n;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

import static org.mockito.Mockito.doReturn;

public class MessagesTest {

    private Locale origLocale;

    private Messages messages;

    @Before
    public void setUp() {

        origLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("key", Locale.US, "found");

        messages = new Messages(messageSource);
    }

    @After
    public void tearDown() {
        Locale.setDefault(origLocale);
    }

    @Test
    public void testGet_validKey() {
        Assert.assertEquals("found", messages.get("key"));
    }

    @Test
    public void testGet_invalidKey() {
        Assert.assertEquals("???notDefined???", messages.get("notDefined"));
    }
}
