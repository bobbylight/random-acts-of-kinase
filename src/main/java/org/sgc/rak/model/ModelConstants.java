package org.sgc.rak.model;

/**
 * Constants for field lengths, etc.
 */
public final class ModelConstants {

    private static final int IP_ADDRESS_MAX = 39;

    public static final int AUDIT_ACTION_MAX = 40;

    public static final int AUDIT_DETAILS_MAX = 500;

    public static final int AUDIT_IP_ADDRESS_MAX = IP_ADDRESS_MAX;

    public static final int AUDIT_USER_NAME_MAX = 40;

    public static final int BLOG_POST_TITLE_MIN = 5;

    public static final int BLOG_POST_TITLE_MAX = 128;

    public static final int BLOG_POST_BODY_MAX = 1048576;

    public static final int COMPOUND_COMPOUND_NAME_MAX = 100;

    public static final int COMPOUND_CHEMOTYPE_MAX = 100;

    public static final int COMPOUND_REFERENCE_MAX = 768;

    public static final int COMPOUND_SOURCE_MAX = 80;

    public static final int COMPOUND_SMILES_MAX = 2048;

    public static final int FEEDBACK_EMAIL_MAX = 254;

    public static final int FEEDBACK_IP_ADDRESS_MAX = IP_ADDRESS_MAX;

    public static final int FEEDBACK_TITLE_MIN = 5;

    public static final int FEEDBACK_TITLE_MAX = 128;

    public static final int FEEDBACK_BODY_MAX = 8000;

    public static final int KINASE_DISCOVERX_GENE_SYMBOL_MAX = 100;

    public static final int KINASE_ENTREZ_GENE_SYMBOL_MAX = 100;

    public static final int KINASE_NANOSYN_GENE_SYMBOL_MAX = 100;

    public static final int PARTNER_IMAGE_MAX = 80;

    public static final int PARTNER_NAME_MAX = 64;

    public static final int URL_MAX = 2048;

    /**
     * Private constructor to prevent instantiation.
     */
    private ModelConstants() {
    }
}
