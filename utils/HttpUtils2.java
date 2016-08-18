import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils2 {

	private static Logger logger = LoggerFactory.getLogger(HttpUtils2.class);

	public static String post(String url, Map<String, String> parameters)
			throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> list = new ArrayList<NameValuePair>();

		Set<Entry<String, String>> entrySet = parameters.entrySet();
		Iterator<Entry<String, String>> it = entrySet.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		UrlEncodedFormEntity postEntity = null;
		try {
			postEntity = new UrlEncodedFormEntity(list, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("参数编码为UTF-8格式时失败，" + e.getMessage() + "\n post url:"
					+ url);
			e.printStackTrace();
		}
		if (postEntity != null) {
			post.setEntity(postEntity);
			// 执行请求
			CloseableHttpResponse httpResponse = null;
			try{
				httpResponse = httpClient.execute(post);
				HttpEntity RespEntity = httpResponse.getEntity();
				if (null != RespEntity) {
					return EntityUtils.toString(RespEntity);
				}
			}finally{
				httpResponse.close();
			}
			
		}
		return StringUtils.EMPTY;
	}

}
