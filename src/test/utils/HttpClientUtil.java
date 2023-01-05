package com.dfd.utils.request;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    /**
     * 配置http请求超时时间 (毫秒)
     */
    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000).setConnectionRequestTimeout(15000).build();

    private static HttpClientUtil httpClientUtil = null;

    /**
     * 初始化构造函数
     * @return
     */
    public static HttpClientUtil getInstance(){
        if(null == httpClientUtil){
            httpClientUtil = new HttpClientUtil();
        }
        return httpClientUtil;
    }

    /**
     * 发送 HTTP_POST 请求
     * @param httpUrl 请求链接 (可以接参数 如： http://baidu.com?a=123&b=456)
     * @return
     * @throws Exception
     */
    public String doHttpPost(String httpUrl) throws Exception{
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 HTTP_POST 请求
     * @param httpUrl 请求链接
     * @param param JSON对象
     * @return
     * @throws Exception
     */
    public String doHttpPost(String httpUrl, JSONObject param) throws Exception{
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置参数
        StringEntity stringEntity = new StringEntity(param.toString(),"UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);

        //创建参数集合
        /*List<NameValuePair> nameValuePairList = new ArrayList<>();
        for(String key : param.keySet()){
            nameValuePairList.add(new BasicNameValuePair(key,param.getString(key)));
        }*/
        //设置参数
       /* StringEntity stringEntity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);*/

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 HTTP_POST 请求
     * @param httpUrl 请求链接
     * @param param 字符串 (格式：a=123&b=456&c=789)
     * @return
     * @throws Exception
     */
    public String doHttpPost(String httpUrl, String param) throws Exception{
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置参数
        StringEntity stringEntity = new StringEntity(param,"UTF-8");
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);

        return sendHttpPost(httpPost);
    }

    /**
     * 发送 HTTP_POST 请求
     * @param httpUrl 请求链接
     * @param map Map<String,String>对象
     * @return
     * @throws Exception
     */
    public String doHttpPost(String httpUrl, Map<String,String> map) throws Exception{
        HttpPost httpPost = new HttpPost(httpUrl);
        //创建参数集合
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        for(String key : map.keySet()){
            nameValuePairList.add(new BasicNameValuePair(key,map.get(key)));
        }
        //设置参数
        StringEntity stringEntity = new UrlEncodedFormEntity(nameValuePairList,"UTF-8");
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 HTTP_DELETE 请求
     * @param httpUrl 请求链接
     * @param authHeader 请求头部 Authorization 参数
     * @return
     * @throws Exception
     */
    public String doHttpDelete(String httpUrl,String authHeader){
        HttpDelete httpDelete = new HttpDelete(httpUrl);
        httpDelete.setHeader("Content-type","application/json");
        httpDelete.setHeader("DataEncoding","UTF-8");
        httpDelete.setHeader("Authorization",authHeader);
        return sendHttpDelete(httpDelete);
    }

    /**
     * 发送 HTTP_POST 请求公共方法
     * @param httpPost
     * @return
     */
    private String sendHttpPost(HttpPost httpPost){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        String result = null;

        try {
            //初始化默认的httpClient实例
            httpClient = HttpClients.createDefault();
            //设置http请求配置项
            httpPost.setConfig(requestConfig);
            //执行请求
            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            //转换响应结果
            result = EntityUtils.toString(httpEntity,"UTF-8");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //关闭连接，释放资源
                if(null != httpResponse){
                    httpResponse.close();
                }
                if(null != httpClient){
                    httpClient.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送 HTTP_DELETE 请求公共方法
     * @param httpDelete
     * @return
     */
    private String sendHttpDelete(HttpDelete httpDelete){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        String result = null;

        try {
            //初始化默认的httpClient实例
            httpClient = HttpClients.createDefault();
            //设置http请求配置项
            httpDelete.setConfig(requestConfig);
            //执行请求
            httpResponse = httpClient.execute(httpDelete);
            httpEntity = httpResponse.getEntity();
            //转换响应结果
            result = EntityUtils.toString(httpEntity,"UTF-8");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //关闭连接，释放资源
                if(null != httpResponse){
                    httpResponse.close();
                }
                if(null != httpClient){
                    httpClient.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return result;
    }

}