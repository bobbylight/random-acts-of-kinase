package org.sgc.rak.model;

/**
 * Constants for field lengths, etc.
 */
public final class ModelConstants {

    public static final int BLOG_POST_TITLE_LENGTH_MIN = 5;

    public static final int BLOG_POST_TITLE_LENGTH_MAX = 128;

    public static final int BLOG_POST_BODY_LENGTH_MAX = 1048576;

    public static final int COMPOUND_COMPOUND_NAME_MAX = 100;

    public static final int COMPOUND_CHEMOTYPE_MAX = 100;

    public static final int COMPOUND_REFERENCE_MAX = 768;

    public static final int COMPOUND_REFERENCE_URL_MAX = 2048;

    public static final int COMPOUND_SOURCE_MAX = 80;

    public static final int COMPOUND_SMILES_MAX = 2048;

    public static final int FEEDBACK_EMAIL_LENGTH_MAX = 254;

    public static final int FEEDBACK_IP_ADDRESS_LENGTH_MAX = 39;

    public static final int FEEDBACK_TITLE_LENGTH_MIN = 5;

    public static final int FEEDBACK_TITLE_LENGTH_MAX = 128;

    public static final int FEEDBACK_BODY_LENGTH_MAX = 8000;

    public static final int KINASE_DISCOVERX_GENE_SYMBOL_MAX = 100;

    public static final int KINASE_ENTREZ_GENE_SYMBOL_MAX = 100;

    /**
     * Private constructor to prevent instantiation.
     */
    private ModelConstants() {
    }
}
