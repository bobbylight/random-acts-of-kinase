package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Valid values for the "modifier" column of {@code NanoBretActivityProfile}.
 */
public enum NanoBretActivityProfileModifier {

    @JsonProperty(">")
    GREATER_THAN,

    @JsonProperty("=")
    EQUAL_TO
}
