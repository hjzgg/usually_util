
import java.util.Collections;
import java.util.List;

public class AbsListResponse<T> extends SimpleResponse {
	

	private static final long serialVersionUID = 7355109290886880716L;
	private List<T> list;//数据列表

	protected List<T> getList() {
		if(list == null){
			return Collections.emptyList();
		}
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public void addObject(T object){
		list.add(object);
	}
	
}