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
import org.sgc.rak.model.csv.ActivityProfileCsvRecord;
import org.sgc.rak.model.csv.KdCsvRecord;
import org.sgc.rak.model.csv.SScoreCsvRecord;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.CompoundService;
import org.sgc.rak.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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

            reader = mapper.readerFor(clazz).with(schema);

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
     * @param file The CSV activity profile data from Discoverx.
     * @param headerRow Whether the CSV data contains a header row.
     * @param commit Whether the upsert should be committed (vs. a dry run with just the results returned).
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

        List<ActivityProfileCsvRecord> activityProfiles = loadFromCsv(file, headerRow,
            ActivityProfileCsvRecord.class, schema);
        return activityProfileService.importActivityProfiles(activityProfiles, commit);
    }

    /**
     * Imports a CSV file of compounds.  This isn't a data file received from Discoverx, but rather just a
     * utility provided for easy bulk modification of compounds.<p>
     *
     * The data is merged/patched into the existing compound data; that is, new compounds are added, and existing
     * compounds have their non-null/empty values merged into the existing compound record.
     *
     * @param file The CSV compound data.
     * @param headerRow Whether the CSV data contains a header row.
     * @param commit Whether the upsert should be committed (vs. a dry run with just the results returned).
     * @return The result of the operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "compounds")
    @ResponseStatus(HttpStatus.OK)
    ObjectImportRep importCompounds(@RequestPart("file") MultipartFile file,
                                    @RequestParam(defaultValue = "true") boolean headerRow,
                                    @RequestParam(defaultValue = "true") boolean commit) {

        CsvSchema schema = CsvSchema.builder()
            .addColumn("compoundName", CsvSchema.ColumnType.STRING)
            .addColumn("chemotype", CsvSchema.ColumnType.STRING)
            .addColumn("s10", CsvSchema.ColumnType.NUMBER)
            .addColumn("smiles", CsvSchema.ColumnType.STRING)
            .addColumn("source", CsvSchema.ColumnType.STRING)
            .addColumn("primaryReference", CsvSchema.ColumnType.STRING)
            .addColumn("primaryReferenceUrl", CsvSchema.ColumnType.STRING)
            .addColumn("hidden", CsvSchema.ColumnType.BOOLEAN)
            .build();

        List<Compound> compounds = loadFromCsv(file, headerRow, Compound.class, schema);
        return compoundService.importCompounds(compounds, commit);
    }

    /**
     * Imports a CSV file of Kd values from Discoverx.  The data is merged/patched into the existing activity
     * profile data; that is, new activity profiles are added, and existing activity profiles have their non-null/empty
     * values merged into the existing record.
     *
     * @param file The CSV Kd data from Discoverx.
     * @param headerRow Whether the CSV data contains a header row.
     * @param commit Whether the upsert should be committed (vs. a dry run with just the results returned).
     * @return The result of the operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "kdValues")
    @ResponseStatus(HttpStatus.OK)
    ObjectImportRep importKdValues(@RequestPart("file") MultipartFile file,
                                   @RequestParam(defaultValue = "true") boolean headerRow,
                                   @RequestParam(defaultValue = "true") boolean commit) {

        CsvSchema schema = CsvSchema.builder()
            .addColumn("compoundName", CsvSchema.ColumnType.STRING)
            .addColumn("discoverxGeneSymbol", CsvSchema.ColumnType.STRING)
            .addColumn("entrezGeneSymbol", CsvSchema.ColumnType.STRING)
            .addColumn("modifier", CsvSchema.ColumnType.STRING)
            .addColumn("kd", CsvSchema.ColumnType.NUMBER)
            .build();

        List<KdCsvRecord> kdValues = loadFromCsv(file, headerRow, KdCsvRecord.class, schema);
        return activityProfileService.importKdValues(kdValues, commit);
    }

    /**
     * Imports a CSV file of S scores (s10) from Discoverx.  The data is merged/patched into the existing activity
     * profile data; that is, new activity profiles are added, and existing activity profiles have their non-null/empty
     * values merged into the existing record.<p>
     *
     * If the CSV data contains any S scores other than s(10)s (for example, s(1)s or s(35)s), they are ignored.
     *
     * @param file The CSV S Score data from Discoverx.
     * @param headerRow Whether the CSV data contains a header row.
     * @param commit Whether the upsert should be committed (vs. a dry run with just the results returned).
     * @return The result of the operation.
     */
    @RequestMapping(method = RequestMethod.PATCH, path = "sScores")
    @ResponseStatus(HttpStatus.OK)
    ObjectImportRep importSScores(@RequestPart("file") MultipartFile file,
                                   @RequestParam(defaultValue = "true") boolean headerRow,
                                   @RequestParam(defaultValue = "true") boolean commit) {

        CsvSchema schema = CsvSchema.builder()
            .addColumn("compoundName", CsvSchema.ColumnType.STRING)
            .addColumn("selectivityScoreType", CsvSchema.ColumnType.STRING)
            .addColumn("numberOfHits", CsvSchema.ColumnType.NUMBER)
            .addColumn("numberOfNonMutantHits", CsvSchema.ColumnType.NUMBER)
            .addColumn("screeningConcentration", CsvSchema.ColumnType.NUMBER)
            .addColumn("selectivityScore", CsvSchema.ColumnType.NUMBER)
            .build();

        List<SScoreCsvRecord> sScores = loadFromCsv(file, headerRow, SScoreCsvRecord.class, schema);

        List<Compound> compounds = sScores.stream()
            .filter(s -> "S(10)".equals(s.getSelectivityScoreType())) // Only care about s(10) for now
            .map(Util::sScoreCsvRecordToCompound)
            .collect(Collectors.toList());
        return compoundService.importCompounds(compounds, commit);
    }
}
