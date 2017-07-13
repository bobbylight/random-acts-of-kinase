package org.sgc.rak.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA entity representing a kinase.
 */
@Entity
@Table(name = "kinase")
public class Kinase {

    @Id
    @Column(name = "id", updatable = false)
    private long id;

    @Column(name = "discoverx_gene_symbol", updatable = false)
    private String discoverxGeneSymbol;

    @Column(name = "entrez_gene_symbol", updatable = false)
    private String entrezGeneSymbol;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("id", id).
            append("discoverxGeneSymbol", discoverxGeneSymbol).
            append("entrezGeneSymbol", entrezGeneSymbol).
            build();
    }
}
