package org.sgc.rak.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * JPA entity representing a compound in the "with kinase activity profile" view.
 */
@Entity
@Table(name = "compound_with_kinase_activity_profile")
public class CompoundWithKinaseActivityProfile extends AbstractCompound {
}
