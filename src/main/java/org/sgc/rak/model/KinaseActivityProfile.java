package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

/**
 * JPA entity representing a kinase activity profile.
 */
@Entity
@Table(name = "kinase_activity_profile")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KinaseActivityProfile {

    @Id
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "compound_nm", updatable = false)
    private String compoundName;

//    @Column(name = "kinase", updatable = false)
//    private int kinase;
    @OneToOne(optional = false)
    @JoinColumn(name = "kinase", nullable = false, updatable = false)
    private Kinase kinase;

    @Column(name = "percent_control", updatable = false)
    private double percentControl;

    @Column(name = "compound_concentration", updatable = false)
    private int compoundConcentration;

    @Column(name = "kd", updatable = false)
    private Double kd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public Kinase getKinase() {
        return kinase;
    }

    public void setKinase(Kinase kinase) {
        this.kinase = kinase;
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
            .append("kinase", kinase)
            .append("percentControl", percentControl)
            .append("compoundConcentration", compoundConcentration)
            .append("kd", kd)
            .build();
    }
}
