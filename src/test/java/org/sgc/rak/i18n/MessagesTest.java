package org.sgc.rak.i18n;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

/**
 * Unit tests for the {@link Messages} class.
 */
public class MessagesTest {

    private Locale origLocale;

    private Messages messages;

    @BeforeEach
    public void setUp() {

        origLocale = Locale.getDefault();
        Locale.setDefault(Locale.US);

        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("key", Locale.US, "found");

        messages = new Messages(messageSource);
    }

    @AfterEach
    public void tearDown() {
        Locale.setDefault(origLocale);
    }

    @Test
    public void testGet_validKey() {
        Assertions.assertEquals("found", messages.get("key"));
    }

    @Test
    public void testGet_invalidKey() {
        Assertions.assertEquals("???notDefined???", messages.get("notDefined"));
    }
}
