package org.sgc.rak.rest;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.PagedDataRep;
import org.springframework.mock.http.MockHttpOutputMessage;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

public class CsvHttpMessageConverterTest {

    private static Compound createCompound(String compoundName, Double s10, String smiles) {
        Compound compound = new Compound();
        compound.setCompoundName(compoundName);
        compound.setS10(s10);
        compound.setSmiles(smiles);
        return compound;
    }

    @Test
    public void testSupports_pagedDataRep() {

        CsvMapper csvMapper = new CsvMapper();

        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        Assertions.assertTrue(converter.supports(PagedDataRep.class));
    }

    @Test
    public void testSupports_unacceptedClassTypes() {

        CsvMapper csvMapper = new CsvMapper();

        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        Assertions.assertFalse(converter.supports(String.class));
        Assertions.assertFalse(converter.supports(null));
    }

    @Test
    public void testRead_happyPath() {
        CsvMapper csvMapper = new CsvMapper();
        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        ParameterizedType type = TypeUtils.parameterize(PagedDataRep.class, Compound.class);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            converter.read(type, PagedDataRep.class, null);
        });
    }

    @Test
    public void testReadInternal_happyPath() {
        CsvMapper csvMapper = new CsvMapper();
        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            converter.readInternal(PagedDataRep.class, null);
        });
    }

    @Test
    public void testWriteInternal_happyPath() throws Exception {

        List<Compound> data = Arrays.asList(
            createCompound("compoundA", 0.3, "smilesA"),
            createCompound("compoundB", null, "smilesB")
        );
        PagedDataRep<Compound> pagedDataRep = new PagedDataRep<>(data, 10, 100);

        CsvMapper csvMapper = new CsvMapper();
        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        ParameterizedType type = TypeUtils.parameterize(PagedDataRep.class, Compound.class);
        converter.writeInternal(pagedDataRep, type, outputMessage);

        String expected =
            "compoundName,chemotype,s10,solubility,smiles,source,primaryReference,primaryReferenceUrl,hidden\n" +
            "compoundA,,0.3,,smilesA,,,,\n" +
            "compoundB,,,,smilesB,,,,\n";
        Assertions.assertEquals(expected, outputMessage.getBodyAsString());
    }
}
