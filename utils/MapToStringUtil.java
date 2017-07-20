import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hujunzheng on 2017/7/20.
 */
public class MapToStringUtil {

    public static String toEqualString(Map<?, ?> map, char separator) {
        List<String> result = new ArrayList<>();
        map.entrySet().parallelStream().reduce(result, (first, second)->{
            first.add(second.getKey() + "=" + second.getValue());
            return first;
        }, (first, second)->{
            if (first == second) {
                return first;
            }
            first.addAll(second);
            return first;
        });

        return StringUtils.join(result, separator);
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "hjz");
        jsonObject.put("age", 25);

        System.out.println(MapToStringUtil.toEqualString(jsonObject, ','));
    }
}