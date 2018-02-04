package org.sgc.rak.rest;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import org.sgc.rak.reps.PagedDataRep;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;

/**
 * An {@code HttpMessageConverter} that can write instances of {@link PagedDataRep} as CSV.  The rep's data will
 * be written as comma-separated records with a header row.  The {@code start}, {@code count}, and {@code total}
 * fields will not be serialized.<p>
 *
 * Reading is not supported since entity uploads in CSV format will be for PATCH operations, and be
 * just records of data, not data for a specific "page" in the collection.  Thus deserializing into an instance of
 * {@code PagedDataRep} does not make sense.
 */
public class CsvHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    /** Cached {@link ObjectWriter}s, indexed by type. */
    private final ConcurrentMap<Class<?>, ObjectWriter> writers = new ConcurrentHashMap<>();
    /** The {@link CsvMapper} to which the heavy lifting is delegated. */
    private final CsvMapper csvMapper;
    /** The character used as value delimiter. */
    private final char delimiter;
    /** Flag indicating whether a header is expected to be read or written. */
    private final boolean includeHeader;

    /**
     * Instantiates a new {@link CsvHttpMessageConverter}.
     *
     * @param supportedMediaType The {@link MediaType} that should be supported by this converter.
     * @param delimiter The character to be used as value delimiter.
     * @param includeHeader Flag indicating whether a header is expected to be read or written.
     * @param csvMapper The {@link CsvMapper} to which the heavy lifting is delegated.
     */
    private CsvHttpMessageConverter(MediaType supportedMediaType, char delimiter,
            boolean includeHeader, CsvMapper csvMapper) {
        super(supportedMediaType);
        this.csvMapper = csvMapper;
        this.delimiter = delimiter;
        this.includeHeader = includeHeader;
    }

    /**
     * Creates a default CSV converter supporting the {@code text/csv} media type.
     *
     * @param csvMapper {@link CsvMapper} used for de-/serialization.
     * @return A {@link CsvHttpMessageConverter} configured for CSV files.
     */
    public static CsvHttpMessageConverter csv(CsvMapper csvMapper) {
        return new CsvHttpMessageConverter(new MediaType("text","csv"), ',', true, csvMapper);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz == PagedDataRep.class;
    }

    @Override
    public List<?> read(Type type, Class<?> contextClazz, HttpInputMessage inputMessage) {
        throw new UnsupportedOperationException("Reading is not supported with this converter");
    }

    @Override
    protected List<?> readInternal(Class<?> clazz, HttpInputMessage inputMessage) {
        throw new UnsupportedOperationException("Reading is not supported with this converter");
    }

    @Override
    protected void writeInternal(Object instance, Type type, HttpOutputMessage outputMessage) throws IOException {

        try (SequenceWriter writer = getWriter(type).writeValues(getOutputWriter(outputMessage));
                Stream<?> stream = createStream(instance)) {
            Iterator<?> it = stream.iterator();
            while (it.hasNext()) {
                writer.write(it.next());
            }
        }
    }

    /**
     * Returns a {@link Writer} for producing the contents of the given {@link HttpOutputMessage}.
     *
     * @param outputMessage The {@link HttpOutputMessage} to which the content should be written.
     * @return A {@link Writer}.
     * @throws IOException If the response body cannot be written to.
     */
    private Writer getOutputWriter(HttpOutputMessage outputMessage) throws IOException {
        /*
         * Respect the charset using the Content-Type header, if any; default to ISO-8859-1
         * otherwise.
         */
        Charset outputCharset = Optional.ofNullable(outputMessage.getHeaders().getContentType())
                .map(MediaType::getCharset)
                .orElse(StandardCharsets.ISO_8859_1);
        return new OutputStreamWriter(outputMessage.getBody(), outputCharset);
    }

    /**
     * Returns the {@link ObjectWriter} for instances of the given class.
     *
     * @param type The type of instances to serialize.
     * @return An {@link ObjectWriter}.
     */
    private ObjectWriter getWriter(Type type) {
        Class<?> parameterType = (Class)((ParameterizedType)type).getActualTypeArguments()[0];
        return this.writers.computeIfAbsent(parameterType, c -> this.csvMapper.writer(getSchema(type)));
    }

    /**
     * Returns the {@link CsvSchema} describing the given class.
     *
     * @param type The type of the object being serialized/deserialized (i.e. {@code PagedDataRep<?>}).
     * @return A {@link CsvSchema}.
     */
    private CsvSchema getSchema(Type type) {
        Class<?> parameterType = (Class)((ParameterizedType)type).getActualTypeArguments()[0];
        CsvSchema schema = this.csvMapper.schemaFor(parameterType).withColumnSeparator(this.delimiter);
        return this.includeHeader ? schema.withHeader() : schema;
    }

    /**
     * Returns the given instance as a {@link Stream}.
     *
     * @param instance The object that should be converted to a stream.
     * @return A {@link Stream} that streams the elements of {@code instance}.
     */
    private Stream<?> createStream(Object instance) {
        return ((PagedDataRep<?>)instance).getData().stream();
    }
}
