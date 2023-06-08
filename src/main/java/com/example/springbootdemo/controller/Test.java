package com.example.springbootdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springbootdemo.untils.Encode;
import com.example.springbootdemo.untils.OkHttpClientUntil;
import com.sun.istack.internal.NotNull;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping(value = "/api/test")
public class Test {

    @Autowired
    private Encode encode;

    @RequestMapping(value="/test1")
    @ResponseBody
    public String test1(){
        try{
            InputStream reportTemplate = this.getClass().getResourceAsStream("/static/sl_mmht.jasper");

            Map<String,Object> parms = new HashMap<String, Object>();
//            InputStream image1 = this.getClass().getResourceAsStream("/static/img1.jpeg");
            parms.put("HTBH","WQ202304210001");
//            parms.put("image2",this.getClass().getClassLoader().getResource("/").getPath() + "img2.jpeg");
            //3.模板和数据整合
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportTemplate, parms, new JREmptyDataSource());
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\loop\\Desktop\\demo.pdf");
            //4.导出PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint,fileOutputStream);
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping(value="/test2")
    @ResponseBody
    public String test2(){
        OkHttpClient client = OkHttpClientUntil.getClient();
        for(int i = 0;i < 5;i ++){
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/test/test1")
                    .method("POST", body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String result = response.body().string();
                System.out.println(result);
            }catch (Exception e){
                System.out.println(e);
            }

//            Call call = client.newCall(request);
//            // 异步调用
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    System.out.println(e.getMessage());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response)
//                        throws IOException {
//                    System.out.println(response.body().toString());
//                }
//            });
        }
        return "success";
    }

    @RequestMapping(value="/test3")
    @ResponseBody
    public String test3(String access_token, String auth_code){
        StringBuffer url = new StringBuffer("http://10.161.251.142/license-app/v1/license/archive?access_token=");
        url.append(access_token).append("&auth_code=").append(auth_code);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        try{
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject resultObject = JSONObject.parseObject(result);
            JSONObject dataObject = resultObject.getJSONObject("data");
            String data = dataObject.getString("file_data");
            byte[] decodeBytes = Base64.getDecoder().decode(data);
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\loop\\Desktop\\离婚证.pdf");
            fileOutputStream.write(decodeBytes);
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }



}
