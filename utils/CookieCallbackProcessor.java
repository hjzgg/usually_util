import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xxx.CookieUtil;

@Controller
public class CookieCallbackProcessor {
	
	@RequestMapping(value="/cookieCallbackProcessor")
	@ResponseBody
	public String getPortalCookie(@RequestParam(value="callback")String callback, HttpServletRequest request) throws UnsupportedEncodingException{
		JSONObject json = new JSONObject();
		Cookie[] cookies = request.getCookies();
		StringBuffer sb = new StringBuffer();
		if(cookies!=null && cookies.length>0){
			for(Cookie cookie : cookies){
				String name = cookie.getName();
				String value = CookieUtil.findCookieValue(cookies, name);
				json.put(name, value);
			}
		}
		sb.append(callback+"("+json.toJSONString()+");");
		return sb.toString();
	}
}

///前端ajax方法问
$.ajax({
  url:'xxx/yyy',
  type:'jsonp',
  jsonp: 'callback',//传递给处理程序，用于获取jsonp回调函数名的参数名（默认：callback）
  jsonpCallback: 'success_callback',//自定义jsonp回调函数名称，默认为jquery生成随机函数名称
  data:{
  },
  success:function(data){
  },
  error: function(data){
  }
})
