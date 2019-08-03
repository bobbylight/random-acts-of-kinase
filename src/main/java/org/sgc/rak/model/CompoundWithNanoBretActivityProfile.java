package org.sgc.rak.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * JPA entity representing a compound in the "with nanobret activity profile" view.
 */
@Entity
@Table(name = "compound_with_nanobret_activity_profile")
public class CompoundWithNanoBretActivityProfile extends AbstractCompound {
}
