package org.sgc.rak.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;
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
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "compound_nm", nullable = false, updatable = false,
        length = ModelConstants.COMPOUND_COMPOUND_NAME_MAX)
    private String compoundName;

//    @Column(name = "kinase", updatable = false)
//    private int kinase;
    @ManyToOne(optional = false)
    @JoinColumn(name = "kinase", nullable = false, updatable = false)
    private Kinase kinase;

    @Column(nullable = false, updatable = false,
        length = ModelConstants.NANOBRET_ACTIVITY_PROFILE_NLUC_ORIENTATION_MAX)
    private String nlucOrientation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private NanoBretActivityProfileModifier modifier;

    @Column(name = "ic50", nullable = false, updatable = false)
    private Double ic50;

    @Column(name = "nanobret_percent_inhibition", nullable = false, updatable = false)
    private Double percentInhibition;

    @Column(name = "concentration", nullable = false, updatable = false)
    private Integer concentration;

    @Column(name = "points", nullable = false, updatable = false)
    private Integer points;

    @Column(name = "comment", nullable = false, updatable = false,
        length = ModelConstants.NANOBRET_ACTIVITY_PROFILE_COMMENT_MAX)
    private String comment;

    @Column(name = "run_date", nullable = false, updatable = false)
    private Date date;

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

    public Double getPercentInhibition() {
        return percentInhibition;
    }

    public void setPercentInhibition(Double percentInhibition) {
        this.percentInhibition = percentInhibition;
    }

    public Integer getConcentration() {
        return concentration;
    }

    public void setConcentration(Integer concentration) {
        this.concentration = concentration;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("compoundName", compoundName)
            .append("kinase", kinase)
            .append("nlucOrientation", nlucOrientation)
            .append("modifier", modifier)
            .append("ic50", ic50)
            .append("percentInhibition", percentInhibition)
            .append("concentration", concentration)
            .append("points", points)
            .append("comment", comment)
            .append("date", date)
            .build();
    }
}
