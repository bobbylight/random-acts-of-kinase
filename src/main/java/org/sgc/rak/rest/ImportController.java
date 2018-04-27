package org.sgc.rak.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.sgc.rak.exceptions.InternalServerErrorException;
import org.sgc.rak.i18n.Messages;
import org.sgc.rak.model.Compound;
import org.sgc.rak.model.KinaseActivityProfile;
import org.sgc.rak.reps.ObjectImportRep;
import org.sgc.rak.services.ActivityProfileService;
import org.sgc.rak.services.CompoundService;
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

    private CompoundService compoundService;
    private ActivityProfileService activityProfileService;

    private Messages messages;

    @Autowired
    ImportController(CompoundService compoundService, ActivityProfileService activityProfileService,
                     Messages messages) {
        this.compoundService = compoundService;
        this.activityProfileService = activityProfileService;
        this.messages = messages;
    }

    private <T> List<T> loadFromCsv(MultipartFile file, Class<T> clazz) {

        String csv;
        try {
            csv = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            throw new InternalServerErrorException(messages.get("error.importingData"));
        }

        CsvMapper mapper = new CsvMapper();
        List<T> data;

        try {
            MappingIterator<T> iter = mapper.readerWithSchemaFor(clazz)
                // Note this doesn't seem to work, so we manually null out empty strings later
                .withFeatures(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .readValues(csv);
            data = iter.readAll();
        } catch (Exception e) {
            throw new InternalServerErrorException(messages.get("error.importingData"));
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
                                           @RequestParam(defaultValue = "true") boolean commit) {
        List<KinaseActivityProfile> activityProfiles = loadFromCsv(file, KinaseActivityProfile.class);
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
        List<Compound> compounds = loadFromCsv(file, Compound.class);
        return compoundService.importCompounds(compounds, commit);
    }
}