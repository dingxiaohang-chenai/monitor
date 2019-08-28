package com.monitor.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author: dingxh
 * @description: 服务监控service
 * @date: 2019/8/27 17:23
 * @version: 1.0
 **/
@Service
public class MonitorService {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private MessageService messageService;

    @Value("${service.para}")
    private String servicePara;

    //@Scheduled(cron = "* */2 * * * ?")
    public void monitor() {
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(servicePara);
        if (0 == serviceInstanceList.size()) {
            messageService.send(servicePara);
        }
        serviceInstanceList.stream()
                .forEach(list -> {
                    System.out.println(list.getScheme());
                    String res = httpToGet("http://" + list.getHost() + ":" + list.getPort() + "/actuator/health");
                    if (null == res || !res.contains("\"status\":\"UP\",\"") || res.contains("connection refused")) {
                        messageService.send(list.getUri().toString());
                    }
                });


    }

    public String httpToGet(String uri) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        String content = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //请求体内容
                content = EntityUtils.toString(response.getEntity(), "UTF-8");

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
        return content;
    }

}