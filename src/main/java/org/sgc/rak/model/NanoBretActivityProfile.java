package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Objects;

/**
 * JPA entity representing a NanoBRET activity profile.
 */
@Entity
@Table(name = "nanobret_activity_profile")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NanoBretActivityProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "compound_nm", updatable = false, length = ModelConstants.COMPOUND_COMPOUND_NAME_MAX)
    private String compoundName;

//    @Column(name = "kinase", updatable = false)
//    private int kinase;
    @ManyToOne(optional = false)
    @JoinColumn(name = "kinase", nullable = false, updatable = false)
    private Kinase kinase;

    @Column(name = "ic50")
    private Double ic50;

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }

        if (obj instanceof NanoBretActivityProfile) {
            NanoBretActivityProfile ap2 = (NanoBretActivityProfile)obj;
            return Objects.equals(id, ap2.id);
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

    public Double getIc50() {
        return ic50;
    }

    public void setIc50(Double ic50) {
        this.ic50 = ic50;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("kinase", kinase)
            .append("ic50", ic50)
            .build();
    }
}
