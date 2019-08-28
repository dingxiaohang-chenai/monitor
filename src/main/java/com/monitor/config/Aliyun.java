package com.monitor.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@EnableConfigurationProperties(Aliyun.class)
@Configuration
@ConfigurationProperties(prefix = "aliyun")
@Data
public class Aliyun {
    private String key;
    private String secret;
    private String smsSignature;
    private Map<String, String> smsTemplates;
    private Map<String, String> email;

    @Bean("aliyunSms")
    IAcsClient aliyunSms(Aliyun aliyun) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyun.getKey(), aliyun.getSecret());
        DefaultProfile.addEndpoint("cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        return new DefaultAcsClient(profile);
    }

    @Bean("aliyunEmail")
    IAcsClient aliyunEmail(Aliyun aliyun) {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyun.getKey(), aliyun.getSecret());
        return new DefaultAcsClient(profile);
    }
}
