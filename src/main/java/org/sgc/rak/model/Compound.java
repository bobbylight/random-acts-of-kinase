package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA entity representing a compound.
 */
@Entity
@Table(name = "compound")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "compoundName", "chemotype", "s10", "smiles", "source", "primaryReference",
    "primaryReferenceUrl", "hidden" })
public class Compound {

    @Id
    @Column(name = "compound_nm", updatable = false, length = ModelConstants.COMPOUND_COMPOUND_NAME_MAX)
    private String compoundName;

    @Column(name = "chemotype", length = ModelConstants.COMPOUND_CHEMOTYPE_MAX)
    private String chemotype;

    @Column(name = "s_10")
    private Double s10;

    @Column(name = "solubility")
    private Double solubility;

    @Column(name = "smiles", length = ModelConstants.COMPOUND_SMILES_MAX)
    private String smiles;

    @Column(name = "source", length = ModelConstants.COMPOUND_SOURCE_MAX)
    private String source;

    @Column(name = "reference_1", length = ModelConstants.COMPOUND_REFERENCE_MAX)
    private String primaryReference;

    @Column(name = "reference_1_url", length = ModelConstants.URL_MAX)
    private String primaryReferenceUrl;

    @Column(nullable = false)
    private Boolean hidden;

    public String getChemotype() {
        return chemotype;
    }

    public void setChemotype(String chemotype) {
        this.chemotype = chemotype;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public Double getS10() {
        return s10;
    }

    public void setS10(Double s10) {
        this.s10 = s10;
    }

    public Double getSolubility() {
        return solubility;
    }

    public void setSolubility(Double solubility) {
        this.solubility = solubility;
    }

    public String getSmiles() {
        return smiles;
    }

    public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPrimaryReference() {
        return primaryReference;
    }

    public void setPrimaryReference(String primaryReference) {
        this.primaryReference = primaryReference;
    }

    public String getPrimaryReferenceUrl() {
        return primaryReferenceUrl;
    }

    public void setPrimaryReferenceUrl(String primaryReferenceUrl) {
        this.primaryReferenceUrl = primaryReferenceUrl;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
            append("compoundName", compoundName).
            append("chemotype", chemotype).
            append("s10", s10).
            append("solubility", solubility).
            append("smiles", smiles).
            append("source", source).
            append("hidden", hidden).
            build();
    }
}
