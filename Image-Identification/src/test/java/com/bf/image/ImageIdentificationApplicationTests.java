package com.bf.image;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ImageIdentificationApplicationTests {

    @Test
    void contextLoads() {
        String robotUrl = "https://oapi.dingtalk.com/robot/send?access_token=15166b6fb3783093b5d937c879f69c57bbcd11fae4aea6b367a6e6f415375fc1";
        String message = "【消息内容】\n" +
                "用户名称：user2\n" +
                "领导名称：user\n" +
                "设备名称：1\n" +
                "设备类型：电缆\n" +
                "上传时图像名称：images.jpg\n" +
                "局部TEV有效值：1.0\n" +
                "局部TEV最大值：1.0\n" +
                "局部TEV最小值：2.0\n" +
                "局部TEV发生时间：2024-04-12 17:04:18\n" +
                "局部TEV最大值：1.0\n" +
                "极性：正极性\n" +
                "局部TEV频率：23\n" +
                "创建时间：2024-04-12 17:04:51\n";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(robotUrl);
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");

            // 构建请求体
            String requestBody = "{\"msgtype\": \"text\", \"text\": {\"content\": \"" + message + "\"}}";
            StringEntity stringEntity = new StringEntity(requestBody, "UTF-8");
            httpPost.setEntity(stringEntity);

            // 发送请求
            CloseableHttpResponse response = httpClient.execute(httpPost);

            // 处理响应
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println("Response: " + responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
