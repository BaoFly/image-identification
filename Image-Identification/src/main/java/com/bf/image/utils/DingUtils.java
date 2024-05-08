package com.bf.image.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.bf.image.exception.CustomException;
import com.bf.image.service.impl.MinIOUServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingUtils {

    public static void sendDing(String url, String messageContent) {
        String responseString = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");

            // 构建请求体
            String requestBody = "{\"msgtype\": \"text\", \"text\": {\"content\": \"" + messageContent + "\"}}";
            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            httpPost.setEntity(stringEntity);

            // 发送请求
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // 处理响应
            HttpEntity responseEntity = response.getEntity();
            responseString = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println("Response: " + responseString);
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            if (StringUtils.isBlank(jsonObject.getString("errmsg"))) {
                throw new CustomException("发送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("发送失败，请检查URL");
        }
    }

}
