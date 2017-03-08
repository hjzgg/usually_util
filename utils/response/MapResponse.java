
import java.util.Collections;
import java.util.Map;

public class MapResponse<K, V> extends SimpleResponse {


	private static final long serialVersionUID = 8808867173761836953L;

	private Map<K, V> map;

	public Map<K, V> getMap() {
		if(map == null){
			return Collections.emptyMap();
		}
		return map;
	}

	public void setMap(Map<K, V> map) {
		this.map = map;
	}
	
	public void addEntry(K key, V value){
		map.put(key, value);
	}
}