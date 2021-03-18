package org.sgc.rak.model;

/**
 * A compound/count pair.  Used to denote the number of activity profiles for
 * a given compound.
 */
public class CompoundCountPair {

    private final String compoundName;
    private final int count;

    public CompoundCountPair(String compoundName, int count) {
        this.compoundName = compoundName;
        this.count = count;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public int getCount() {
        return count;
    }
}
