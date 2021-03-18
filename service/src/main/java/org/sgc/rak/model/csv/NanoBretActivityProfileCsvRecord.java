package org.sgc.rak.model.csv;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.sgc.rak.model.NanoBretActivityProfileModifier;

/**
 * A record representing a NanoBRET activity profile in a CSV file from SGC ({@code *_???.csv}).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "compoundName", "discoverxGeneSymbol", "nlucOrientation", "modifier", "ic50",
    "percentInhibition", "compoundConcentration", "points", "comment", "date" })
public class NanoBretActivityProfileCsvRecord {

    private String compoundName;

    private String discoverxGeneSymbol;

    private String nlucOrientation;

    private NanoBretActivityProfileModifier modifier;

    private Double ic50;

    private Double percentInhibition;

    private Integer compoundConcentration;

    private Integer points;

    private String comment;

    private String date; // YY_MM_DD format

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

    public String getNlucOrientation() {
        return nlucOrientation;
    }

    public void setNlucOrientation(String nlucOrientation) {
        this.nlucOrientation = nlucOrientation;
    }

    public NanoBretActivityProfileModifier getModifier() {
        return modifier;
    }

    public void setModifier(NanoBretActivityProfileModifier modifier) {
        this.modifier = modifier;
    }

    public Double getIc50() {
        return ic50;
    }

    public void setIc50(Double ic50) {
        this.ic50 = ic50;
    }

    public Double getPercentInhibition() {
        return percentInhibition;
    }

    public void setPercentInhibition(Double percentInhibition) {
        this.percentInhibition = percentInhibition;
    }

    public Integer getCompoundConcentration() {
        return compoundConcentration;
    }

    public void setCompoundConcentration(Integer compoundConcentration) {
        this.compoundConcentration = compoundConcentration;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("discoverxGeneSymbol", discoverxGeneSymbol)
            .append("nlucOrientation", nlucOrientation)
            .append("modifier", modifier)
            .append("ic50", ic50)
            .append("percentInhibition", percentInhibition)
            .append("compoundConcentration", compoundConcentration)
            .append("points", points)
            .append("comment", comment)
            .append("date", date)
            .build();
    }
}
