
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RefTreeResponse implements Serializable {
	private static final long serialVersionUID = 1909805035274106578L;

	private List<RefTreeResponse> children;

	private String id;

	private String code;

	private String name;

	private String key;
	
	private boolean parent;

	public List<RefTreeResponse> getChildren() {
		return children;
	}

	public void setChildren(List<RefTreeResponse> children) {
		this.children = children;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isParent() {
		return parent;
	}

	public void setParent(boolean parent) {
		this.parent = parent;
	}

	public boolean addChildren(RefTreeResponse child) {
		if (null == children)
			children = new ArrayList<>();
		return this.children.add(child);
	}
}
