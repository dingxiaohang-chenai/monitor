package com.monitor;

import com.monitor.base.TestBase;
import com.monitor.service.MonitorService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;


public class MonitorTests extends TestBase{
    @Autowired
    private MonitorService monitorService;

    @Test
    public void sendTest(){
        monitorService.monitor();
    }











    @Test
    public void httpToGetTest() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");

                System.out.println("====内容====>" + content);
                System.out.println("内容长度：" + content.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                    //相当于关闭浏览器
                    httpclient.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
