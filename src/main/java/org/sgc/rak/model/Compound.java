package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Compound {

    @Id
    @Column(name = "compound_nm", updatable = false)
    private String compoundName;

    @Column(name = "chemotype", updatable = false)
    private String chemotype;

    @Column(name = "s_10", updatable = false)
    private Double s10;

    @Column(name = "smiles", updatable = false)
    private String smiles;

    @Column(name = "source", updatable = false)
    private String source;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
            append("compoundName", compoundName).
            append("chemotype", chemotype).
            append("s10", s10).
            append("smiles", smiles).
            append("source", source).
            build();
    }
}
