package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

/**
 * JPA entity representing a kinase activity profile.
 */
@Entity
@Table(name = "kinase_activity_profile")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "compound_nm", updatable = false, length = ModelConstants.COMPOUND_COMPOUND_NAME_MAX)
    private String compoundName;

//    @Column(name = "kinase", updatable = false)
//    private int kinase;
    @OneToOne(optional = false)
    @JoinColumn(name = "kinase", nullable = false, updatable = false)
    private Kinase kinase;

    @Column(name = "percent_control")
    private Double percentControl;

    @Column(name = "compound_concentration")
    private Integer compoundConcentration;

    @Column(name = "kd")
    private Double kd;

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof ActivityProfile) {
            ActivityProfile kap2 = (ActivityProfile)obj;
            return Objects.equals(id, kap2.id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : Long.hashCode(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Double getPercentControl() {
        return percentControl;
    }

    public void setPercentControl(Double percentControl) {
        this.percentControl = percentControl;
    }

    public Integer getCompoundConcentration() {
        return compoundConcentration;
    }

    public void setCompoundConcentration(Integer compoundConcentration) {
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
