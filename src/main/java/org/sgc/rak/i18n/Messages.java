package org.sgc.rak.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Provides messages in the request's locale.
 */
public class Messages {

    private MessageSource messageSource;

    public Messages(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Returns text loclized in the request locale.
     *
     * @param key The key in the resource bundle of the text to retrieve.
     * @param params Any parameters needed in the text.
     * @return The localized text.
     */
    public String get(String key, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, params, locale);
    }
}
