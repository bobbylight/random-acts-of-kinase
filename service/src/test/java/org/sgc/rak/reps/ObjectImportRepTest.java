package org.sgc.rak.reps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObjectImportRepTest {

    private ObjectImportRep importRep;

    @BeforeEach
    public void setUp() {
        importRep = new ObjectImportRep();
    }

    @Test
    public void testGetSetFieldStatuses() {

        Assertions.assertEquals(0, importRep.getFieldStatuses().size());

        List<ObjectImportRep.FieldStatus> fields = new ArrayList<>();
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        status.setFieldName("compoundName");
        status.setNewValue("compoundA");
        status.setOldValue("wrongValue");
        fields.add(status);

        List<List<ObjectImportRep.FieldStatus>> fieldStatuses = Collections.singletonList(fields);
        importRep.setFieldStatuses(fieldStatuses);

        Assertions.assertEquals(1, importRep.getFieldStatuses().size());
        ObjectImportRep.FieldStatus actualStatus = importRep.getFieldStatuses().get(0).get(0);
        Assertions.assertEquals(status.getFieldName(), actualStatus.getFieldName());
    }

    @Test
    public void testFieldStatus_getSetNewValue() {
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        Assertions.assertNull(status.getNewValue());
        status.setNewValue("foo");
        Assertions.assertEquals("foo", status.getNewValue());
    }

    @Test
    public void testFieldStatus_getSetOldValue() {
        ObjectImportRep.FieldStatus status = new ObjectImportRep.FieldStatus();
        Assertions.assertNull(status.getOldValue());
        status.setOldValue("foo");
        Assertions.assertEquals("foo", status.getOldValue());
    }
}
