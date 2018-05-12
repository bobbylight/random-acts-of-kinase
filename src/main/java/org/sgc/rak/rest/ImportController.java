package org.sgc.rak.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.sgc.rak.exceptions.BadRequestException;
import org.sgc.rak.exceptions.InternalServerErrorException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.reps.ActivityProfileCsvRecordRep;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.CompoundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * REST API for importing data.
 */
@RestController
@RequestMapping(path = "/admin/api")
public class ImportController {

    private final CompoundService compoundService;
    private final ActivityProfileService activityProfileService;

    private final Messages messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    ImportController(CompoundService compoundService, ActivityProfileService activityProfileService,
                     Messages messages) {
        this.compoundService = compoundService;
        this.activityProfileService = activityProfileService;
        this.messages = messages;
    }

    private <T> List<T> loadFromCsv(MultipartFile file, boolean headerRow, Class<T> clazz, CsvSchema schema) {

        String csv;
        try {
            csv = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            throw new InternalServerErrorException(messages.get("error.importingData"));
        }

        CsvMapper mapper = new CsvMapper();
        ObjectReader reader;
        List<T> data;

        if (headerRow) {
            schema = schema.withHeader();
        }

        try {

            if (schema == null) {
                reader = mapper.readerWithSchemaFor(clazz);
            }
            else {
                reader = mapper.readerFor(clazz).with(schema);
            }

            MappingIterator<T> iter = reader
                // Note this doesn't seem to work, so we manually null out empty strings later
                .withFeatures(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .withFeatures(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS)
                .readValues(csv);
            data = iter.readAll();
        } catch (Exception e) {
            LOGGER.error("Error deserializing CSV into " + clazz.getSimpleName() + ": " + e.getMessage(),
                LOGGER.isDebugEnabled() ? e : null);
            throw new BadRequestException(messages.get("error.invalidCsvFormat"));
        }

        if (data.isEmpty()) {
            throw new BadRequestException(messages.get("error.noDataInFile"));
        }

        return data;
    }

    /**
     * Imports a CSV file of activity profiles.  The data is merged/patched into the existing activity profile data;
     * that is, new activity profiles are added, and existing activity profiles have their non-null/empty values
     * merged into the existing record.
     *
     * @param file The CSV compound data.
     * @return The result of the operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "activityProfiles")
    @ResponseStatus(HttpStatus.OK)
    ObjectImportRep importActivityProfiles(@RequestPart("file") MultipartFile file,
                                           @RequestParam(defaultValue = "true") boolean headerRow,
                                           @RequestParam(defaultValue = "true") boolean commit) {

        CsvSchema schema = CsvSchema.builder()
            .addColumn("compoundName", CsvSchema.ColumnType.STRING)
            .addColumn("discoverxGeneSymbol", CsvSchema.ColumnType.STRING)
            .addColumn("entrezGeneSymbol", CsvSchema.ColumnType.STRING)
            .addColumn("percentControl", CsvSchema.ColumnType.NUMBER)
            .addColumn("compoundConcentration", CsvSchema.ColumnType.NUMBER)
            .build();

        List<ActivityProfileCsvRecordRep> activityProfiles = loadFromCsv(file, headerRow,
            ActivityProfileCsvRecordRep.class, schema);
        return activityProfileService.importActivityProfiles(activityProfiles, commit);
    }

    /**
     * Imports a CSV file of compounds.  The data is merged/patched into the existing compound data;
     * that is, new compounds are added, and existing compounds have their non-null/empty values
     * merged into the existing compound record.
     *
     * @param file The CSV compound data.
     * @return The result of the operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "compounds")
    @ResponseStatus(HttpStatus.OK)
    ObjectImportRep importCompounds(@RequestPart("file") MultipartFile file,
                                      @RequestParam(defaultValue = "true") boolean commit) {
        List<Compound> compounds = loadFromCsv(file, false, Compound.class, null);
        return compoundService.importCompounds(compounds, commit);
    }
}
