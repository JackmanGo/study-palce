package place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import place.param.WeChatSessionResponse;

/**
 * @author wangxi
 * @date 2025/9/11 21:37
 */
@Service
public class WeChatService {

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeChatSessionResponse getSessionInfo(String jsCode) {
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                "wxaf01d5b66bf15b39", "98046c4d2c7d97a24c08bc39e1e77fc7", jsCode);

        //return restTemplate.getForObject(url, WeChatSessionResponse.class);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                return objectMapper.readValue(responseBody, WeChatSessionResponse.class);
            } else {
                throw new RuntimeException("微信接口请求失败: " + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("获取微信会话信息失败", e);
        }
    }



    public String getOpenId(String jsCode) {
        WeChatSessionResponse sessionInfo = getSessionInfo(jsCode);
        if (sessionInfo.isSuccess()) {
            return sessionInfo.getOpenId();
        } else {
            throw new RuntimeException("获取OpenID失败: " + sessionInfo.getErrMsg());
        }
    }
}
