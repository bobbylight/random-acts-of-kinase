package org.sgc.rak.reps;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result (or possible result) of an import of compounds.
 */
public class CompoundImportRep {

    private List<CompoundStatusPair> compoundStatuses;

    public CompoundImportRep() {
        this.compoundStatuses = new ArrayList<>();
    }

    public List<CompoundStatusPair> getCompoundStatuses() {
        return compoundStatuses;
    }

    public void setCompoundStatuses(List<CompoundStatusPair> compoundStatuses) {
        this.compoundStatuses = compoundStatuses != null ? compoundStatuses : new ArrayList<>();
    }

    public static enum Status {
        NEW_COMPOUND,
        UPDATED_COMPOUND
    }

    public static class CompoundStatusPair {

        private String compoundName;
        private Status status;

        public CompoundStatusPair(String compoundName, Status status) {
            this.compoundName = compoundName;
            this.status = status;
        }

        public String getCompoundName() {
            return compoundName;
        }

        public void setCompoundName(String compoundName) {
            this.compoundName = compoundName;
        }

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }
    }
}
