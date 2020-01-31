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
@JsonPropertyOrder({ "compoundName", "chemotype", "s10", "solubility", "smiles", "source", "primaryReference",
    "primaryReferenceUrl", "hidden" })
public class Compound extends AbstractCompound {
}
