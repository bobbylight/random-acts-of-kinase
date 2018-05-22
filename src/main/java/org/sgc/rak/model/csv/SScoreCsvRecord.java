package org.sgc.rak.model.csv;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A record representing a S-Score in a CSV file from discoverx ({@code *_SScores Report.csv}).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "compoundName", "selectivityScoreType", "numberOfHits", "numberOfNonMutantKinases",
    "screeningConcentration", "selectivityScore" })
public class SScoreCsvRecord {

    private String compoundName;
    private String selectivityScoreType;
    private Integer numberOfHits;
    private Integer numberOfNonMutantHits;
    private Double screeningConcentration;
    private Double selectivityScore;

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getSelectivityScoreType() {
        return selectivityScoreType;
    }

    public void setSelectivityScoreType(String selectivityScoreType) {
        this.selectivityScoreType = selectivityScoreType;
    }

    public Integer getNumberOfHits() {
        return numberOfHits;
    }

    public void setNumberOfHits(Integer numberOfHits) {
        this.numberOfHits = numberOfHits;
    }

    public Integer getNumberOfNonMutantHits() {
        return numberOfNonMutantHits;
    }

    public void setNumberOfNonMutantHits(Integer numberOfNonMutantHits) {
        this.numberOfNonMutantHits = numberOfNonMutantHits;
    }

    public Double getScreeningConcentration() {
        return screeningConcentration;
    }

    public void setScreeningConcentration(Double screeningConcentration) {
        this.screeningConcentration = screeningConcentration;
    }

    public Double getSelectivityScore() {
        return selectivityScore;
    }

    public void setSelectivityScore(Double selectivityScore) {
        this.selectivityScore = selectivityScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("selectivityScoreType", selectivityScoreType)
            .append("numberOfHits", numberOfHits)
            .append("numberOfNonMutantHits", numberOfNonMutantHits)
            .append("screeningConcentration", screeningConcentration)
            .append("selectivityScore", selectivityScore)
            .build();
    }
}
