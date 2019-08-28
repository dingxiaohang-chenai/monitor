package com.monitor.service;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.monitor.config.Aliyun;
import com.monitor.error.ServiceError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author: dingxh
 * @description: 消息发送service
 * @date: 2019/8/27 17:36
 * @version: 1.0
 **/
@Service
@Slf4j
public class MessageService {
    @Autowired
    @Qualifier("aliyunEmail")
    private IAcsClient aliyunEmail;
    @Autowired
    @Qualifier("aliyunSms")
    private IAcsClient aliyunSms;
    @Autowired
    private Aliyun aliyun;

    @Value("${send.email}")
    private String email;
    @Value("${send.region}")
    private String region;
    @Value("${send.mobile}")
    private String mobile;

    @Async
    public void send(String para) {
        if (para == null) {
            log.error("para not present");
            return;
        }

        emailAliyun(email, para);
        //mobileAliyun(region, mobile, para);
    }

    private void emailAliyun(String email, String para) {
        SingleSendMailRequest request = new SingleSendMailRequest();
        String from = aliyun.getEmail().get("from");
        String alias = aliyun.getEmail().get("alias");
        String tag = aliyun.getEmail().get("tag");
        request.setAccountName(from);
        request.setFromAlias(alias);
        request.setAddressType(1);
        request.setTagName(tag);
        request.setReplyToAddress(false);
        //可以给多个收件人发送邮件，收件人之间用逗号分开，批量发信建议使用BatchSendMailRequest方式
        request.setToAddress(email);
        request.setSubject("服务下线通知,请及时处理");
        request.setHtmlBody("详情: \r\n<b>" + para + "</b>。");

        SingleSendMailResponse response;
        try {
            response = aliyunEmail.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            throw ServiceError.INTERNAL_ERROR;
        }
        log.error("发送结果 ：response {}", response.getRequestId());
    }

    private void mobileAliyun(String region, String mobile, String prot) {
        //阿里云去掉首位的0
        if (mobile.startsWith("0"))
            mobile = mobile.substring(1);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        request.setSignName(aliyun.getSmsSignature());
        String phoneNumber, templateCode;
        if ("86".equals(region)) {
            phoneNumber = mobile;
            templateCode = aliyun.getSmsTemplates().get("zh");
        } else {
            phoneNumber = region + mobile;
            templateCode = aliyun.getSmsTemplates().get("en");
        }
        request.setPhoneNumbers(phoneNumber);
        request.setTemplateCode(templateCode);

        request.setTemplateParam("{\"off port\":\"" + prot + "\"}");

        SendSmsResponse response;
        try {
            response = aliyunSms.getAcsResponse(request);
        } catch (ClientException e) {
            log.error(e.getMessage(), e);
            throw ServiceError.INTERNAL_ERROR;
        }
        if (!"OK".equals(response.getCode()))
            throw ServiceError.INTERNAL_ERROR;
    }
}