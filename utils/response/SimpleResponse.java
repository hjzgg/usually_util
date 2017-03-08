
import java.io.Serializable;

/**
 * http请求返回数据
 * @param <T> 请求返回数据类型
 */
public class SimpleResponse implements Serializable {
	private static final long serialVersionUID = -5238115197608725618L;

	//返回的标识码
	private ReturnCode code = ReturnCode.SUCCESS;
	//返回的消息
	private String msg;

	public SimpleResponse() {}
	public SimpleResponse(boolean flag) {
		if(flag) {
			this.code = ReturnCode.SUCCESS;
			this.msg = "操作成功";
		}else {
			this.code = ReturnCode.FAILURE;
			this.msg = "操作失败";
		}
	}

	public String getCode() {
		return code.getValue();
	}

	/**
	 * 设置http请求返回的标识码
	 * 
	 * @param code 标志码
	 * @see ReturnCode 
	 */
	public void setCode(ReturnCode code) {
		this.code = code;
	}
	
	public void setCode(boolean flag) {
		if(flag){
			this.code = ReturnCode.SUCCESS;
		}
		else{
			this.code = ReturnCode.FAILURE;
		}
	}

	public String getMsg() {
		return msg;
	}

	/**
	 * 设置返回的消息
	 * @param msg
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
