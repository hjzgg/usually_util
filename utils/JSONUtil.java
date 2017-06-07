import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JSONUtil {
    private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();
    private static ObjectMapper dynamicMapper = new ObjectMapper();
    private static ObjectMapper caseMapper = new ObjectMapper();
    private static ObjectMapper caseDynamicMapper = new ObjectMapper();
    private static ObjectMappingCustomer selfMapper = new ObjectMappingCustomer();
    private static ObjectMappingCustomer dynamicSelfMapper = new ObjectMappingCustomer();


    public static class ObjectMappingCustomer extends ObjectMapper {

        private static final long serialVersionUID = 1L;

        public ObjectMappingCustomer() {
            super();
            this.getSerializerProvider().setNullKeySerializer(new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                        throws IOException, JsonProcessingException {
                    gen.writeString("beiquan-null-key");
                }
            });
            this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {

                @Override
                public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers)
                        throws IOException, JsonProcessingException {
                    gen.writeString("");
                }
            });

        }
    }

    static {
        dynamicMapper.setSerializationInclusion(Include.NON_NULL);
        dynamicMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dynamicMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        caseDynamicMapper.setSerializationInclusion(Include.NON_NULL);
        caseDynamicMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        caseDynamicMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        caseDynamicMapper.setPropertyNamingStrategy(new LowerCasePropertyStrategy());
        caseMapper.setPropertyNamingStrategy(new LowerCasePropertyStrategy());

        dynamicSelfMapper.setSerializationInclusion(Include.NON_EMPTY);
        dynamicSelfMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        dynamicSelfMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);


    }

    public JSONUtil() {
    }

    public static String objToStr(Object obj) {
        return objToStr(obj, false);
    }

    public static String objToStr(Object obj, boolean writeNull) {
        try {
            return writeNull ? mapper.writeValueAsString(obj) : dynamicMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Error: {}",e);
            return null;
        }
    }
    public static <T> T strToObj(String str, Class<T> claz) {
        return strToObj(str, claz, false);
    }

    public static <T> T strToObj(String str, Class<T> claz, boolean writeNull) {
        try {
            return writeNull ? mapper.readValue(str, claz) : dynamicMapper.readValue(str, claz);
        } catch (Exception e) {
            logger.error("Error: {},{}",str,e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cloneObj(T obj) {
        return (T) cloneObj(obj, obj.getClass());
    }

    public static <T> T cloneObj(Object obj, Class<T> claz) {
        try {
            return dynamicMapper.readValue(dynamicMapper.writeValueAsString(obj), claz);
        } catch (Exception e) {
            logger.error("Error: {},{}",claz.getName(),e);
            return null;
        }
    }

    /**属性名称转小写**/
    public static <T> T strToObjLowerCase(String str, Class<T> claz) {
        return strToObjLowerCase(str, claz, false);
    }

    /**解码的时候,所有大写字母转小写**/
    public static <T> T strToObjLowerCase(String str, Class<T> claz, boolean writeNull) {
        try {
            return writeNull ? caseMapper.readValue(str, claz) : caseDynamicMapper.readValue(str, claz);
        } catch (Exception e) {
            logger.error("Error: {},{}",str,e);
            return null;
        }
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    public static String objToStrNew(Object obj) {
        return objToStrNew(obj, false);
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    public static String objToStrNew(Object obj, boolean writeNull) {
        try {
            return writeNull ? selfMapper.writeValueAsString(obj) : dynamicSelfMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("Error: {}",e);
            return null;
        }
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    public static <T> T strToObjNew(String str, Class<T> claz) {
        return strToObj(str, claz, false);
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    public static <T> T strToObjNew(String str, Class<T> claz, boolean writeNull) {
        try {
            return writeNull ? selfMapper.readValue(str, claz) : dynamicSelfMapper.readValue(str, claz);
        } catch (Exception e) {
            logger.error("Error: {},{}",str,e);
            return null;
        }
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    @SuppressWarnings("unchecked")
    public static <T> T cloneObjNew(T obj) {
        return (T) cloneObj(obj, obj.getClass());
    }

    /**-----------------返回结果自动处理null-->""------------------**/
    public static <T> T cloneObjNew(Object obj, Class<T> claz) {
        try {
            return dynamicSelfMapper.readValue(dynamicSelfMapper.writeValueAsString(obj), claz);
        } catch (Exception e) {
            logger.error("Error: {},{}",claz.getName(),e);
            return null;
        }
    }

}
