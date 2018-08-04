<!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl -->
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>${codehaus.jackson-version}</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-core-asl -->
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-core-asl</artifactId>
    <version>${codehaus.jackson-version}</version>
</dependency>

<!--json 转 properties-->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-properties</artifactId>
    <version>2.8.3</version>
</dependency>

<!--xml 转 pojo -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>2.8.3</version>
</dependency>



import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsSchema;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;


public class JacksonHelper {
    private static Logger LOGGER = LoggerFactory.getLogger(JacksonHelper.class);

    /*java bean 转 properties配置*/
    private static final JavaPropsSchema javaPropsSchema;

    private static final SimpleModule module = initModule();
    private static final ObjectMapper objectMapper;
    private static final JavaPropsMapper javaPropsMapper;
    private static final XmlMapper xmlMapper;
    private static final ObjectMapper prettyMapper;

    public JacksonHelper() {
    }

    private static SimpleModule initModule() {
        return (new SimpleModule()).addSerializer(BigDecimal.class, new BigDecimalSerializer())
                .addSerializer(LocalTime.class, new LocalTimeSerializer())
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer())
                .addSerializer(LocalDate.class, new LocalDateSerializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer())
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    }

    private static JacksonXmlModule initXmlModule() {
        JacksonXmlModule jacksonXmlModule = new JacksonXmlModule();
        jacksonXmlModule.addSerializer(BigDecimal.class, new BigDecimalSerializer())
                .addSerializer(LocalTime.class, new LocalTimeSerializer())
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer())
                .addSerializer(LocalDate.class, new LocalDateSerializer())
                .addDeserializer(LocalDate.class, new LocalDateDeserializer())
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer())
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        return jacksonXmlModule;
    }

    public static JavaType genJavaType(Type type) {
        return getObjectMapper().getTypeFactory().constructType(type);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static ObjectMapper getPrettyMapper() {
        return prettyMapper;
    }

    static {
        xmlMapper = new XmlMapper(initXmlModule());
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        objectMapper = (new ObjectMapper()).registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        javaPropsMapper = new JavaPropsMapper();
        javaPropsMapper.registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        javaPropsSchema = JavaPropsSchema.emptySchema().withFirstArrayOffset(0).withWriteIndexUsingMarkers(true);

        prettyMapper = objectMapper.copy().configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    /**
     * json string convert properties
     */
    public static Properties jsonToProperties(String jsonStr) throws ServiceException {
        try {
            // parse JSON
            JsonNode jsonNodeTree = objectMapper.readTree(jsonStr);
            // resolve to properties
            Properties properties = javaPropsMapper.writeValueAsProperties(jsonNodeTree);
            return properties;
        } catch (Exception e) {
            throw new ServiceException("500", String.format("json转properties异常，json=%s", jsonStr), e);
        }
    }

    /**
     * object convert properties
     */
    public static Properties objectToProperties(Object object, String keyPrefix) throws ServiceException {
        try {
            if (Objects.isNull(keyPrefix)) {
                keyPrefix = StringUtils.EMPTY;
            }
            Properties properties = javaPropsMapper.writeValueAsProperties(object, javaPropsSchema.withLineIndentation(keyPrefix));
            return properties;
        } catch (Exception e) {
            throw new ServiceException("500", String.format("object转properties异常，object=%s", object), e);
        }
    }

    /**
     * json string convert to xml string
     */
    public static String json2xml(String jsonStr) throws ServiceException {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            String xml = xmlMapper.writeValueAsString(jsonNode);
            return xml;
        } catch (Exception e) {
            throw new ServiceException("500", "json转xml异常...", e);
        }
    }


    public static String json2xml(String jsonStr, String rootPrefix, String rootSuffix) throws ServiceException {
        String xml = json2xml(jsonStr);
        xml = xml.replace("<ObjectNode>", rootPrefix);
        xml = xml.replace("</ObjectNode>", rootSuffix);
        return xml;
    }

    /**
     * xml string convert to json string
     */
    public static String xml2json(String xml) throws ServiceException {
        try {
            JsonNode node = xmlMapper.readTree(xml);
            String json = objectMapper.writeValueAsString(node);
            return json;
        } catch (Exception e) {
            throw new ServiceException("500", "xml转json异常...", e);
        }
    }
}

class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    public LocalDateDeserializer() {
    }

    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateString = ((JsonNode) jp.getCodec().readTree(jp)).asText();
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    public LocalDateTimeDeserializer() {
    }

    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateTimeStr = ((JsonNode) jp.getCodec().readTree(jp)).asText();
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}

class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    public LocalTimeDeserializer() {
    }

    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateString = ((JsonNode) jp.getCodec().readTree(jp)).asText();
        return LocalTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_TIME);
    }
}

class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    public BigDecimalSerializer() {
    }

    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(value.toString());
    }
}

class LocalDateSerializer extends JsonSerializer<LocalDate> {
    public LocalDateSerializer() {
    }

    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(DateTimeFormatter.ISO_LOCAL_DATE.format(value));
    }
}

class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    public LocalDateTimeSerializer() {
    }

    public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value));
    }
}

class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    public LocalTimeSerializer() {
    }

    public void serialize(LocalTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString(DateTimeFormatter.ISO_LOCAL_TIME.format(value));
    }
}

