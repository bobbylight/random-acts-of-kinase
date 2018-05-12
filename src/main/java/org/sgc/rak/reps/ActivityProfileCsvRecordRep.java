package org.sgc.rak.reps;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A record representing an activity profile in a CSV file from discoverx.<p>
 *
 * Note only the fields we need to create activity profile records are enumerated; other fields are ignored.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "compoundName", "discoverxGeneSymbol", "entrezGeneSymbol",
    "percentControl", "compoundConcentration" })
public class ActivityProfileCsvRecordRep {

    private String compoundName;

    private String discoverxGeneSymbol;

    private String entrezGeneSymbol;

    private double percentControl;

    private int compoundConcentration;

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

    public double getPercentControl() {
        return percentControl;
    }

    public void setPercentControl(double percentControl) {
        this.percentControl = percentControl;
    }

    public int getCompoundConcentration() {
        return compoundConcentration;
    }

    public void setCompoundConcentration(int compoundConcentration) {
        this.compoundConcentration = compoundConcentration;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("discoverxGeneSymbol", discoverxGeneSymbol)
            .append("entrezGeneSymbol", entrezGeneSymbol)
            .append("percentControl", percentControl)
            .append("compoundConcentration", compoundConcentration)
            .build();
    }
}
