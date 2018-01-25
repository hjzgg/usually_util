package xxx.api.dto.validator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.ele.contract.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hujunzheng hujunzheng
 * @create 2018-01-08 下午12:49
 **/
@Component
public class ValidatorUtils implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ValidatorUtils.validator = (Validator) applicationContext.getBean("validator");
    }

    public static class ValidatorGroup {
        public interface First{}
        public interface Second{}
        public interface Third{}
    }

    private static Validator validator;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> validateResultProcess(Object obj) throws ServiceException {
        Set<ConstraintViolation<Object>> results = validator.validate(obj);
        if (CollectionUtils.isEmpty(results)) {
            return Optional.empty();
        }

        List<ErrorMessage> errorMessages = results.stream().map(result -> {
            try {
                List<ErrorMessage> childErrorMessages = objectMapper.readValue(result.getMessage(), new TypeReference<List<ErrorMessage>>() {});
                return childErrorMessages;
            } catch (Exception e) {
                ErrorMessage errorMessage = new ErrorMessage();
                errorMessage.setPropertyPath(String.format("%s.%s", result.getRootBeanClass().getSimpleName(), result.getPropertyPath().toString()));
                errorMessage.setMessage(result.getMessage());
                return Arrays.asList(errorMessage);
            }
        }).flatMap(errorMessageList -> errorMessageList.stream()).collect(Collectors.toList());

        try {
            return Optional.of(objectMapper.writeValueAsString(errorMessages));
        } catch (JsonProcessingException e) {
            throw new ServiceException("JsonProcessingException " + e.getMessage());
        }
    }

    public static Optional<String> validateResultProcesWithId(Object obj, String idFieldName) throws ServiceException {
        Optional<String> result = ValidatorUtils.validateResultProcess(obj);
        if (result.isPresent()) {
            ErrorMessageWithId errorMessageWithId = new ErrorMessageWithId();
            try {
                PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(obj.getClass(), idFieldName);
                errorMessageWithId.setId(pd.getReadMethod().invoke(obj).toString());
            } catch (Exception e) {
                errorMessageWithId.setId("");
            }
            errorMessageWithId.setErrorMessage(result.get());
            try {
                return Optional.of(objectMapper.writeValueAsString(errorMessageWithId));
            } catch (JsonProcessingException e) {
                throw new ServiceException("JsonProcessingException " + e.getMessage());
            }
        } else {
            return result;
        }
    }

    public static void validateResultProcessWithException(Object obj) throws ServiceException {
        Optional<String> validateResult = ValidatorUtils.validateResultProcess(obj);
        if (validateResult.isPresent()) {
            throw new ServiceException(validateResult.get());
        }
    }

    public static void validateResultProcesWithIdAndException(Object obj, String idFieldName) throws ServiceException {
        Optional<String> validateResult = ValidatorUtils.validateResultProcesWithId(obj, idFieldName);
        if (validateResult.isPresent()) {
            throw new ServiceException(validateResult.get());
        }
    }

    public static class ErrorMessage {
        private String propertyPath;
        private String message;

        public String getPropertyPath() {
            return propertyPath;
        }

        public void setPropertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ErrorMessageWithId {
        private String id;
        private String errorMessage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
