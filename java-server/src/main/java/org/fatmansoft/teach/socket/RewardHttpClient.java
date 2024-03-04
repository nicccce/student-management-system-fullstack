package org.fatmansoft.teach.socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;  
import java.util.List;  
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.config.RequestConfig;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.codec.Base64;

public class RewardHttpClient {

    public static void main(String[] args) throws Exception{  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();  
        formparams.add(new BasicNameValuePair("year", "2022"));
        formparams.add(new BasicNameValuePair("rewardCode", "1"));
        formparams.add(new BasicNameValuePair("collegeId", "42"));
        formparams.add(new BasicNameValuePair("user", "gaozheng"));
        HttpEntity reqEntity = new UrlEncodedFormEntity(formparams, "utf-8");  
    
        RequestConfig requestConfig = RequestConfig.custom()  
        .setConnectTimeout(5000)//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间  
                .setSocketTimeout(5000)// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间  
                .setConnectionRequestTimeout(5000)  
                .build();  
    
        HttpClient client = new DefaultHttpClient();  
//        HttpPost post = new HttpPost("https://intergrade.sdu.edu.cn/login");  
        HttpPost post = new HttpPost("http://localhost:8080/api/auth/achievementRewardCollegeSummaryPdfDataStr");
        post.setEntity(reqEntity);  
        post.setConfig(requestConfig);  
        HttpResponse response = client.execute(post);  
    
        if (response.getStatusLine().getStatusCode() == 200) {  
            HttpEntity resEntity = response.getEntity();  
            String message = EntityUtils.toString(resEntity, "utf-8");
            byte[] data = Base64.decode(message.getBytes());
            FileOutputStream out = new FileOutputStream("c:\\temp\\test.pdf");
            out.write(data);
            out.close();
        } else {
            System.out.println("请求失败");  
        }  
    }  

}
