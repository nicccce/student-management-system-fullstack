package com.teach.javafxclient.request;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.gson.reflect.TypeToken;
import com.teach.javafxclient.AppStore;
import com.google.gson.Gson;
import com.teach.javafxclient.model.StudentTableEntity;
import com.teach.javafxclient.util.CommonMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.URI;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.nio.file.Path;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import java.net.URLEncoder;
import com.alibaba.fastjson.JSON;

/**
 * HttpRequestUtil 后台请求实例程序，主要实践向后台发送请求的方法
 *  static boolean isLocal 业务处理程序实现方式 false java-server实现 前端程序通过下面的方法把数据发送后台程序，后台返回前端需要的数据，true 本地方式 业务处理 在SQLiteJDBC 实现
 *  String serverUrl = "http://localhost:9090" 后台服务的机器地址和端口号
 */
public class HttpRequestUtil<T> {
    public static boolean isLocal = false;
    private static Gson gson = new Gson();
    private static HttpClient client = HttpClient.newHttpClient();
    public static final String serverUrl = "http://localhost:9090";

    private static Logger logger = Logger.getLogger(HttpRequestUtil.class);

    /**
     *  应用关闭是需要做关闭处理
     */
    public static void close(){
        if(isLocal)
            SQLiteJDBC.getInstance().close();
    }

    /**
     * String login(LoginRequest request)  用户登录请求实现
     * @param request  username 登录账号 password 登录密码
     * @return  返回null 登录成功 AppStore注册登录账号信息 非空，登录错误信息
     */
    public static String login(LoginRequest request){
        if(isLocal) {
            return SQLiteJDBC.getInstance().login(request.getUsername(),request.getPassword());
        }else {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/api/auth/login"))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .headers("Content-Type", "application/json")
                    .build();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    JwtResponse jwt = gson.fromJson(response.body(), JwtResponse.class);
                    AppStore.setJwt(jwt);
                    return null;
                } else if (response.statusCode() == 401) {
                    return "用户名或密码不存在！";
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e);
            }
            return "登录失败";
        }
    }

    /**
     * DataResponse request(String url,DataRequest request) 一般数据请求业务的实现
     * @param url  Web请求的Url 对用后的 RequestMapping
     * @param request 请求参数对象
     * @return DataResponse 返回后台返回数据
     */
    public static DataResponse request(String url,DataRequest request){
        if(isLocal) {
            int index = url.lastIndexOf('/');
            String methodName = url.substring(index+1,url.length());
            try {
                Method method = SQLiteJDBC.class.getMethod(methodName, DataRequest.class);
                return (DataResponse)method.invoke(SQLiteJDBC.getInstance(), request);
            }catch(Exception e) {
                logger.error(e);
            }
        }else {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + url))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", "Bearer " + AppStore.getJwt().getAccessToken())
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    DataResponse dataResponse = gson.fromJson(response.body(), DataResponse.class);
                    return dataResponse;
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e);
            }
        }
        return null;
    }

    private Class<T> clazz;

    public HttpRequestUtil(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * DataResponse request(String url, DataRequest request) 面向对象数据请求业务的实现
     *
     * @param url     Web请求的Url，对应后台的RequestMapping
     * @param request 请求参数对象
     * @return DataResponse 返回后台返回的数据
     */
    public DataResponse requestArrayList(String url,DataRequest request){
        if(isLocal) {
            int index = url.lastIndexOf('/');
            String methodName = url.substring(index+1,url.length());
            try {
                Method method = SQLiteJDBC.class.getMethod(methodName, DataRequest.class);
                return (DataResponse)method.invoke(SQLiteJDBC.getInstance(), request);
            }catch(Exception e) {
                logger.error(e);
            }
        }else {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + url))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", "Bearer " + AppStore.getJwt().getAccessToken())
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    /*DataResponse<T> dataResponse = JSON.parseObject(response.body(), new TypeReference<DataResponse<T>>() {});*/
                    DataResponse<ArrayList<Map>> mapResponse = gson.fromJson(response.body(), DataResponse.class);
                    /*TypeToken<DataResponse<T>> typeToken = new TypeToken<DataResponse<T>>() {};
                    DataResponse<T> dataResponse = gson.fromJson(response.body(),typeToken.getType());*/
                    ArrayList<T> dataList = new ArrayList<>();
                    if(mapResponse != null && mapResponse.getCode()== 0) {
                        for (Map map : mapResponse.getData()){
                            T t = JSONObject.parseObject(JSONObject.toJSONString(map), clazz);
                            dataList.add(t);
                        }
                    }
                    DataResponse<ArrayList<T>> dataResponse = new DataResponse<>(mapResponse.getCode(),dataList, mapResponse.getMsg());
                    return dataResponse;
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e);
            }
        }
        return null;
    }

    /**
     * DataResponse request(String url, DataRequest request) 面向对象数据请求业务的实现
     *
     * @param url     Web请求的Url，对应后台的RequestMapping
     * @param request 请求参数对象
     * @return DataResponse 返回后台返回的数据
     */
    public DataResponse requestObject(String url,DataRequest request){
        if(isLocal) {
            int index = url.lastIndexOf('/');
            String methodName = url.substring(index+1,url.length());
            try {
                Method method = SQLiteJDBC.class.getMethod(methodName, DataRequest.class);
                return (DataResponse)method.invoke(SQLiteJDBC.getInstance(), request);
            }catch(Exception e) {
                logger.error(e);
            }
        }else {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + url))
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .headers("Content-Type", "application/json")
                    .headers("Authorization", "Bearer " + AppStore.getJwt().getAccessToken())
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    /*DataResponse<T> dataResponse = JSON.parseObject(response.body(), new TypeReference<DataResponse<T>>() {});*/
                    DataResponse<ArrayList<Map>> objectResponse = gson.fromJson(response.body(), DataResponse.class);
                    /*TypeToken<DataResponse<T>> typeToken = new TypeToken<DataResponse<T>>() {};
                    DataResponse<T> dataResponse = gson.fromJson(response.body(),typeToken.getType());*/
                    T data = null;
                    if(objectResponse != null && objectResponse.getCode()== 0) {
                            data = JSONObject.parseObject(JSONObject.toJSONString(objectResponse.getData()), clazz);
                    }
                    DataResponse<T> dataResponse = new DataResponse<>(objectResponse.getCode(),data, objectResponse.getMsg());
                    return dataResponse;
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e);
            }
        }
        return null;
    }

    /**
     *  MyTreeNode requestTreeNode(String url, DataRequest request) 获取树节点对象
     * @param url  Web请求的Url 对用后的 RequestMapping
     * @param request 请求参数对象
     * @return MyTreeNode 返回后台返回数据
     */
    public static MyTreeNode requestTreeNode(String url, DataRequest request){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .headers("Authorization", "Bearer "+AppStore.getJwt().getAccessToken())
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String>  response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                return gson.fromJson(response.body(), MyTreeNode.class);
            }
        } catch (IOException e) {
            ;
        } catch (InterruptedException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     *  List<OptionItem> requestOptionItemList(String url, DataRequest request) 获取OptionItemList对象
     * @param url  Web请求的Url 对用后的 RequestMapping
     * @param request 请求参数对象
     * @return List<OptionItem> 返回后台返回数据
     */
    public static List<OptionItem> requestOptionItemList(String url, DataRequest request){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .headers("Authorization", "Bearer "+AppStore.getJwt().getAccessToken())
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String>  response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                OptionItemList o = gson.fromJson(response.body(), OptionItemList.class);
                if(o != null) {
                    return o.getItemList();
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     *   List<OptionItem> getDictionaryOptionItemList(String code) 获取数据字典OptionItemList对象
     * @param code  数据字典类型吗
     * @return List<OptionItem> 返回后台返回数据
     */
    public static  List<OptionItem> getDictionaryOptionItemList(String code) {
        DataRequest req = new DataRequest();
        req.put("code", code);
        return requestOptionItemList("/api/base/getDictionaryOptionItemList",req);
    }

    /**
     *  byte[] requestByteData(String url, DataRequest request) 获取byte[] 对象 下载数据文件等
     * @param url  Web请求的Url 对用后的 RequestMapping
     * @param request 请求参数对象
     * @return List<OptionItem> 返回后台返回数据
     */
    public static byte[] requestByteData(String url, DataRequest request){
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + url))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                .headers("Content-Type", "application/json")
                .headers("Authorization", "Bearer "+AppStore.getJwt().getAccessToken())
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<byte[]>  response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
            if(response.statusCode() == 200) {
                return response.body();
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * DataResponse uploadFile(String fileName,String remoteFile) 上传数据文件
     * @param fileName  本地文件名
     * @param remoteFile 远程文件路径
     * @return 上传操作信息
     */
    public static DataResponse uploadFile(String fileName,String remoteFile)  {
        try {
            Path file = Path.of(fileName);
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl+"/api/base/uploadFile?uploader=HttpTestApp&remoteFile="+remoteFile + "&fileName="
                            + file.getFileName()))
                    .POST(HttpRequest.BodyPublishers.ofFile(file))
                    .build();
            HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                DataResponse dataResponse = gson.fromJson(response.body(), DataResponse.class);
                return dataResponse;
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * DataResponse importData(String url, String fileName, String paras) 导入数据文件
     * @param url  Web请求的Url 对用后的 RequestMapping
     * @param fileName 本地文件名
     * @param paras  上传参数
     * @return 导入结果信息
     */
    public static DataResponse importData(String url, String fileName, String paras)  {
        try {
            Path file = Path.of(fileName);
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl+url+"?uploader=HttpTestApp&fileName="
                            + file.getFileName() + "&"+paras))
                    .POST(HttpRequest.BodyPublishers.ofFile(file))
                    .build();
            HttpResponse<String>  response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200) {
                DataResponse dataResponse = gson.fromJson(response.body(), DataResponse.class);
                return dataResponse;
            }
        } catch (IOException | InterruptedException e) {
            logger.error(e);
        }
        return null;
    }

    /**
     * DataResponse int uploadHtmlString(String html) 加密上传html模板字符串，用于生成htmp网页和PDF文件
     * @param html 上传的HTML字符串
     * @return html 序列号
     */
    public static int uploadHtmlString(String html)  {
            DataRequest req = new DataRequest();
            String str = new String(Base64.getEncoder().encode(html.getBytes(StandardCharsets.UTF_8)));
            req.put("html", str);
            DataResponse res =request("/api/base/uploadHtmlString",req);
            return CommonMethod.getIntegerFromObject(res.getData());
    }

    public static DataResponse getRequest(String url, String pathVariable, DataRequest request) {
        if (!isLocal) {
            // 添加路径参数到 URL
            String fullUrl = serverUrl + url + "/" + pathVariable;

            // 将请求参数添加到 URL 中
            if (request != null && !request.isEmpty()) {
                StringBuilder queryBuilder = new StringBuilder();
                for (Map.Entry<String, Object> entry : request.getData().entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    queryBuilder.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
                    queryBuilder.append("=");
                    queryBuilder.append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8));
                    queryBuilder.append("&");
                }
                // 去除末尾的"&"
                if (queryBuilder.length() > 0) {
                    queryBuilder.setLength(queryBuilder.length() - 1);
                }
                fullUrl += "?" + queryBuilder.toString();
            }

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(fullUrl))
                    .GET()
                    .header("Authorization", "Bearer " + AppStore.getJwt().getAccessToken())
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            try {
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    DataResponse dataResponse = gson.fromJson(response.body(), DataResponse.class);
                    return dataResponse;
                }
            } catch (IOException | InterruptedException e) {
                logger.error(e);
            }
        }
        return null;
    }
}
