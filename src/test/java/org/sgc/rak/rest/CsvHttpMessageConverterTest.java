package org.sgc.rak.rest;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.Assert;
import org.junit.Test;
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
        Assert.assertTrue(converter.supports(PagedDataRep.class));
    }

    @Test
    public void testSupports_unacceptedClassTypes() {

        CsvMapper csvMapper = new CsvMapper();

        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        Assert.assertFalse(converter.supports(String.class));
        Assert.assertFalse(converter.supports(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRead_happyPath() {
        CsvMapper csvMapper = new CsvMapper();
        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        ParameterizedType type = TypeUtils.parameterize(PagedDataRep.class, Compound.class);
        converter.read(type, PagedDataRep.class, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testReadInternal_happyPath() {
        CsvMapper csvMapper = new CsvMapper();
        CsvHttpMessageConverter converter = CsvHttpMessageConverter.csv(csvMapper);
        converter.readInternal(PagedDataRep.class, null);
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
            "compoundName,chemotype,s10,smiles,source,primaryReference,primaryReferenceUrl,hidden,solubility\n" +
            "compoundA,,0.3,smilesA,,,,false,\n" +
            "compoundB,,,smilesB,,,,false,\n";
        Assert.assertEquals(expected, outputMessage.getBodyAsString());
    }
}
