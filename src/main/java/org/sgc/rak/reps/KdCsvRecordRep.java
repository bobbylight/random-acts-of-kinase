package org.sgc.rak.reps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A record representing a KD report in a CSV file from discoverx.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "compoundName", "discoverxGeneSymbol", "entrezGeneSymbol",
    "modifier", "kd" })
public class KdCsvRecordRep {

    private String compoundName;

    private String discoverxGeneSymbol;

    private String entrezGeneSymbol;

    private String modifier;

    private Double kd;

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getDiscoverxGeneSymbol() {
        return discoverxGeneSymbol;
    }

    public void setDiscoverxGeneSymbol(String discoverxGeneSymbol) {
        this.discoverxGeneSymbol = discoverxGeneSymbol;
    }

    public String getEntrezGeneSymbol() {
        return entrezGeneSymbol;
    }

    public void setEntrezGeneSymbol(String entrezGeneSymbol) {
        this.entrezGeneSymbol = entrezGeneSymbol;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Double getKd() {
        return kd;
    }

    public void setKd(Double kd) {
        this.kd = kd;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("discoverxGeneSymbol", discoverxGeneSymbol)
            .append("entrezGeneSymbol", entrezGeneSymbol)
            .append("modifier", modifier)
            .append("kd", kd)
            .build();
    }
}
