package org.sgc.rak.reps;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the result (or possible result) of an importing data (compounds, activity profiles, etc.).
 */
public class ObjectImportRep {

    private List<List<FieldStatus>> fieldStatuses;

    public ObjectImportRep() {
        this.fieldStatuses = new ArrayList<>();
    }

    public List<List<FieldStatus>> getFieldStatuses() {
        return fieldStatuses;
    }

    public void setFieldStatuses(List<List<FieldStatus>> fieldStatuses) {
        this.fieldStatuses = fieldStatuses != null ? fieldStatuses : new ArrayList<>();
    }

    public static class FieldStatus {

        private String fieldName;
        private Object oldValue;
        private Object newValue;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public void setOldValue(Object oldValue) {
            this.oldValue = oldValue;
        }

        public Object getNewValue() {
            return newValue;
        }

        public void setNewValue(Object newValue) {
            this.newValue = newValue;
        }
    }
}
