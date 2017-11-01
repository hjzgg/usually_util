import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class JacksonHelper {
    private static final SimpleModule module = initModule();
    private static final ObjectMapper mapper;
    private static final ObjectMapper prettyMapper;

    public JacksonHelper() {
    }

    private static SimpleModule initModule() {
        return (new SimpleModule()).addSerializer(BigDecimal.class, new BigDecimalSerializer()).addSerializer(LocalTime.class, new LocalTimeSerializer()).addDeserializer(LocalTime.class, new LocalTimeDeserializer()).addSerializer(LocalDate.class, new LocalDateSerializer()).addDeserializer(LocalDate.class, new LocalDateDeserializer()).addSerializer(LocalDateTime.class, new LocalDateTimeSerializer()).addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
    }

    public static JavaType genJavaType(Type type) {
        return getMapper().getTypeFactory().constructType(type);
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }

    public static ObjectMapper getPrettyMapper() {
        return prettyMapper;
    }

    static {
        mapper = (new ObjectMapper()).registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        prettyMapper = mapper.copy().configure(SerializationFeature.INDENT_OUTPUT, true);
    }
}

class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    public LocalDateDeserializer() {
    }

    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateString = ((JsonNode)jp.getCodec().readTree(jp)).asText();
        return LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
    }
}

class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    public LocalDateTimeDeserializer() {
    }

    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateTimeStr = ((JsonNode)jp.getCodec().readTree(jp)).asText();
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}

class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    public LocalTimeDeserializer() {
    }

    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateString = ((JsonNode)jp.getCodec().readTree(jp)).asText();
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

