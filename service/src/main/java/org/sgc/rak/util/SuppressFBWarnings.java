package org.sgc.rak.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to suppress a FindBugs warning.  This is part of the FindBugs annotation
 * library, but the library doesn't actually care what package this annotation is
 * found in, so we add it here to save a dependency.  For more information, see:
 * https://sourceforge.net/p/findbugs/feature-requests/298/#5e88
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {

    /**
     * The set of FindBugs warnings that are to be suppressed in
     * annotated element. The value can be a bug category, kind or pattern.
     *
     */
    String[] value() default {};

    /**
     * Optional documentation of the reason why the warning is suppressed.
     */
    String justification() default "";
}
