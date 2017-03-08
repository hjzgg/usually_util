
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping({ "/message" })
public class TenantMCheckController {
	
	private Logger logger = LoggerFactory.getLogger(TenantMCheckController.class);
	
	private static final int EMAIL_TTL = 86400;
	
	@Autowired
	private IUserTenantAndSupplierService userTSService;
	
	@Autowired
	private ITenantService tenantService;
	
	@Autowired
	private RedisCacheTemplate redisCacheTemplate;

	@RequestMapping(value = { "checkMessage" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public JSONObject CheckMessage(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String phoneNum = request.getParameter("phone");
		String vali = request.getParameter("validate");
		String ran = (String) this.redisCacheTemplate.get(phoneNum);
		if (StringUtils.isEmpty(ran)) {
			result.put("status", Integer.valueOf(0));
			result.put("msg", "手机验证码已经过期，请重新获取短息验证码!");
		} else {
			if (vali.equals(ran)) {
				result.put("status", Integer.valueOf(1));
				result.put("msg", "短息验证成功!");
				this.redisCacheTemplate.delete(phoneNum);
			}
			else {
				result.put("status", Integer.valueOf(0));
				result.put("msg", "手机验证码输入错误，请确认之后重新输入!");
			}
		}
		return result;
	}

	@RequestMapping(value = { "sendMessage" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> sendMessage(HttpServletRequest request) {
		Map<String, Object> results = new LinkedHashMap<String, Object>();
		Date date = new Date();
		String phoneNum = request.getParameter("phone");
		String clientIp = getLocalIp(request);
		if (!(validateIp(clientIp, date).booleanValue())) {
			results.put("msg", "error");
			return results;
		}

		String timeStr = (String) this.redisCacheTemplate.get(phoneNum + "time");
		if (StringUtils.isNotBlank(timeStr)) {
			long ltime = Long.parseLong(timeStr);
			if ((date.getTime() - ltime) / 1000L < 60L) {
				this.redisCacheTemplate.put(phoneNum + "time", date.getTime() + "", 60);
				results.put("msg", "一分钟内不能重复发送验证码");
				return results;
			}

		}

		if (Validate.isMobile(phoneNum)) {
			MessageReceiver mr = new MessageReceiver(phoneNum);
			String rand = ProduceNumber.produce();
			String msg = "验证码: " + rand + "，您正在进行xx云服务用户注册，（xx客服绝对不会索要该验证码，切勿告诉他人），感谢您的支持!";
			MessageContent mc = new SMSContent("短信验证", msg, 0);
			List<MessageResponse> snds = new MessageSend(mr, mc).send();

			this.redisCacheTemplate.put(phoneNum, rand, 300);

			this.redisCacheTemplate.put(phoneNum + "time", date.getTime() + "", 60);
			results.put("status", ((MessageResponse) snds.get(0)).getResponseStatusCode());
			results.put("msg", ((MessageResponse) snds.get(0)).getResponseContent());
		} else {
			results.put("msg", "您输入的手机号不合法");
			results.put("statuc", "0");
		}
		return results;
	}

	public String getLocalIp(HttpServletRequest request) {
		String remoteAddr = request.getRemoteAddr();
		String forwarded = request.getHeader("X-Forwarded-For");
		String realIp = request.getHeader("X-Real-IP");

		String ip = null;
		if (realIp == null) {
			if (forwarded == null)
				ip = remoteAddr;
			else {
				ip = remoteAddr + "/" + forwarded.split(",")[0];
			}
		} else if (realIp.equals(forwarded)) {
			ip = realIp;
		} else {
			if (forwarded != null) {
				forwarded = forwarded.split(",")[0];
			}
			ip = realIp + "/" + forwarded;
		}

		return ip;
	}

	public Boolean validateIp(String ip, Date date) {
		String times = (String) this.redisCacheTemplate.get(ip);
		if (StringUtils.isBlank(times)) {
			this.redisCacheTemplate.put(ip + "date", date.getTime() + "", 60);
			times = "1";
			this.redisCacheTemplate.put(ip, times, 60);
		} else {
			this.redisCacheTemplate.put(ip, (Integer.parseInt(times) + 1) + "", 60);
			times = (Integer.parseInt(times) + 1) + "";
		}
		boolean flag = true;
		Long seconds = Long.valueOf((date.getTime() - Long.parseLong((String) this.redisCacheTemplate.get(ip + "date"))) / 1000L);
		int t = Integer.parseInt(times);
		if ((seconds.longValue() < 60L) && (t > 5)) {
			flag = false;
		}
		return Boolean.valueOf(flag);
	}
	
	
	
	
	@RequestMapping(value = { "sendEmail" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public ObjectResponse<String> getActivationEmail(HttpServletRequest request) {
		ObjectResponse<String> result = new ObjectResponse<String>();
		String service = request.getParameter("service");
		String userCode = request.getParameter("userCode");
		UserBaseVO user = this.userTSService.findByUserCode(userCode);
		
		result.setCode(ReturnCode.FAILURE);
		if (user == null) {
			result.setMsg("用户不存在");
			return result;
		}
		if (user.getTypeId() != 1) {
			result.setMsg("只有管理员才可以申请激活租户");
			return result;
		}
		
		TenantBO tenant = this.tenantService.getPubTenantById(user.getTenantId());
		if (tenant == null) {
			result.setMsg("请先注册租户信息");
			return result;
		}
		if (tenant.getTenantStates() == 1) {
			result.setMsg("租户已经激活");
			return result;
		}
		String checkMsg = canSendEmail(user.getUserEmail());
		if (StringUtils.isNotBlank(checkMsg)) {
			result.setMsg(checkMsg);
			return result;
		}
		try {
			sendEmail(user, service);
			result.setCode(ReturnCode.SUCCESS);
			result.setMsg("租户已经激活");
		} catch (Exception e) {
			e.printStackTrace();
			reduceSendEmailCount(user.getUserEmail());
			result.setMsg("邮件发送出错，请稍后再试");
		}
		return result;
	}
	
	private void reduceSendEmailCount(String email) {
		String cacheKey = "SEND_EMAIL_COUNT_" + email;
		Integer count = (Integer) this.redisCacheTemplate.get(cacheKey);
		if (count == null) {
			return;
		}
		count = Integer.valueOf(count.intValue() - 1);
		if (count.intValue() > 0) {
			this.redisCacheTemplate.put(cacheKey, count, EMAIL_TTL);
		} else {
			this.redisCacheTemplate.delete(cacheKey);
		}
	}
	
	private String canSendEmail(String email) {
		String cacheKey = "SEND_EMAIL_COUNT_" + email;
		Integer count = (Integer) this.redisCacheTemplate.get(cacheKey);
		if (count == null) {
			this.redisCacheTemplate.put(cacheKey, Integer.valueOf(1), EMAIL_TTL);
		} else {
			if (count.intValue() > 3) {
				return "24小时内申请激活超过3次";
			}
			count = Integer.valueOf(count.intValue() + 1);
			this.redisCacheTemplate.put(cacheKey, count, EMAIL_TTL);
		}
		return "";
	}
	
	private void sendEmail(UserBaseVO user, String service) {
		String secretKey = UUID.randomUUID().toString();
		Timestamp outDate = new Timestamp(System.currentTimeMillis() + 86400000L);
		long date = outDate.getTime() / 1000L * 1000L;
		user.setOutDate(outDate.toString());
		user.setSecretKey(secretKey);
		user.setMailValidatect(null);
		this.userTSService.updateUser(user);

		String key = user.getUserName() + "$" + date + "$" + secretKey + "$" + service;
		String digitalSignature = DigesterUtil.encode("MD5", key);
		String hostname = PropertyUtil.getPropertyByKey("hostname");
		String basePath = hostname + "tenant/activation";
		String activationHref = basePath + "?sid=" + digitalSignature + "&uid=" + user.getUserId() + "&service="
				+ service;

		MessageReceiver mr = new MessageReceiver(user.getUserEmail());
		String title = "租户激活";
		String emailContent = "请勿回复本邮件.点击下面的链接,激活租户<br/><a href=" + activationHref + " target='_BLANK'>"
				+ activationHref + "</a>  或者    <a href=" + activationHref + " target='_BLANK'>点击我激活租户</a>"
				+ "<br/>tips:本邮件超过1天,链接将会失效，需要重新申请激活!";

		MessageContent mc = new EmailContent(title, emailContent);
		long start = System.currentTimeMillis();
		List<MessageResponse> sends = new MessageSend(mr, mc).send();
		MessageResponse result = sends.get(0);
		long end = System.currentTimeMillis();
		System.out.println("发送邮件耗时：" + (end - start));
		System.out.println("邮件发送状态：code=" + result.getResponseStatusCode() + " content=" + result.getResponseContent());
	}
	
	
	@RequestMapping(value="istelexist/{tel}", method=RequestMethod.GET)
	@ResponseBody
	public JSONObject isTelExist(@PathVariable("tel") String tel) {
		JSONObject result = new JSONObject();
		try {
			if(userTSService.isTelExist(tel)) {
				result.put("status", 1);
				result.put("flag", 1);
			} else {
				result.put("status", 1);
				result.put("flag", 0);
			}
		} catch (Exception e) {
			result.put("status", 0);
			result.put("message", "内部异常");
			result.put("flag", 0);
		}
		return result;
	}
}