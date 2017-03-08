
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yyjz.icop.data.redis.service.impl.RedisCacheTemplate;
import com.yyjz.icop.tenancy.tenant.web.validateImage.ValidateCode;

@Controller
@RequestMapping({ "/images" })
public class ImageValidatorController {
	private Logger log;

	@Autowired
	private RedisCacheTemplate redisCacheTemplate;

	public ImageValidatorController() {
		this.log = LoggerFactory.getLogger(super.getClass());
	}

	@RequestMapping(value = { "getValiImage" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public void getValiImage(HttpServletRequest request, HttpServletResponse response) {
		String key = request.getParameter("ts");

		ValidateCode vCode = null;
		try {
			vCode = new ValidateCode(120, 40, 4, 40);
		} catch (NoSuchAlgorithmException e) {
			this.log.error(e.getMessage(), e);
			return;
		}
		this.redisCacheTemplate.put(key + vCode.getCode().toLowerCase(), vCode.getCode().toLowerCase(), 60);
		try {
			vCode.write(response.getOutputStream());
		} catch (IOException e) {
			this.log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = { "validateCode" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> validateCode(HttpServletRequest request) {
		Map<String, Object> results = new LinkedHashMap<String, Object>();

		String key = request.getParameter("key");
		String code = request.getParameter("code");
		String saveVCode = (String) this.redisCacheTemplate.get(key + code.toLowerCase());
		if (StringUtils.isNotBlank(saveVCode)) {
			this.redisCacheTemplate.delete(key);
			results.put("msg", "success");
			results.put("status", Integer.valueOf(1));
		} else {
			results.put("msg", "failture");
			results.put("status", Integer.valueOf(0));
		}
		return results;
	}
}