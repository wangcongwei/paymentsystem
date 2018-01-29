package com.newtouch.payment.utils;


import java.io.InputStream;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
import org.apache.http.Header;  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpVersion;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.CookieStore;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ClientConnectionManager;  
import org.apache.http.conn.params.ConnManagerParams;  
import org.apache.http.conn.scheme.PlainSocketFactory;  
import org.apache.http.conn.scheme.Scheme;  
import org.apache.http.conn.scheme.SchemeRegistry;  
import org.apache.http.cookie.Cookie;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;  
import org.apache.http.impl.cookie.BasicClientCookie;  
import org.apache.http.message.BasicHeader;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.params.BasicHttpParams;  
import org.apache.http.params.HttpParams;  
import org.apache.http.params.HttpProtocolParams;  
import org.apache.http.protocol.HTTP; 

public class DwrTest {  
private DefaultHttpClient httpclient;  
      
    private Header[] headers = {new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; " +  
            "Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)"),   
            new BasicHeader("Accept-Language", "zh-cn"),   
            new BasicHeader("Accept", " image/gif, image/x-xbitmap, image/jpeg, " +  
                    "image/pjpeg, application/x-silverlight, application/vnd.ms-excel, " +  
                    "application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*"),   
            new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),   
            new BasicHeader("Accept-Encoding", "gzip, deflate")};  
      
    private Cookie cookie;  
      
    /**  
     * 登录请求的地址  
     */  
    private String action;  
      
    /** 
     * 用户名 
     */  
    private String username;  
      
    /** 
     * 密码 
     */  
    private String password;  
      
    /** 
     * 请求的其他参数 
     */  
    private Map<String, String> map = new HashMap<String, String>();  
      
    public static void main(String[] args) throws Exception {  
        String action = "http://localhost:8888/j_spring_security_check";  
        String username = "admin";  
        String password = "admin";  
        Map<String, String> map = new HashMap<String, String>();  
        map.put("j_username", username);  
        map.put("j_password", password);  
          
        DwrTest dwrTest = new DwrTest();  
        dwrTest.setAction(action);  
        dwrTest.setMap(map);  
        String html = dwrTest.login();  // 登录  
        // System.out.println(html);  
          
        action = "http://localhost:8888/dwr/engine.js";  
        map.clear();  
        dwrTest.setAction(action);  
        html = dwrTest.execute();  
          
        String regEx = "dwr\\.engine\\._origScriptSessionId = \"(\\w+)\"";  
        String scriptSessionId = null;  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(html);  
        while (m.find()) {  
            scriptSessionId = m.group(1);  
        }  
          
        DefaultHttpClient httpClient = dwrTest.getHttpclient();  
        CookieStore cookes = httpClient.getCookieStore();  
        List<Cookie> list = cookes.getCookies();  
        String httpSessionId = "";  
        for (Cookie cookie : list) {  
            String cookieName = cookie.getName();  
            if ("JSESSIONID".equals(cookieName)) {  
                httpSessionId = cookie.getValue();  
            }  
        }  
          
        Header[] headers = {new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; " +  
        "Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)"),   
        new BasicHeader("Accept-Language", "zh-cn"),  
        new BasicHeader("Content-Type", "text/plain"),   
        new BasicHeader("Accept-Encoding", "gzip, deflate")};  
        action = "http://localhost:8888/dwr/call/plaincall/dwrLieBiaoService.getWorkflowList.dwr";  
        map.clear();  
        map.put("model", "CNPC_GWLIST");  
        map.put("httpSessionId", httpSessionId);  
        map.put("scriptSessionId", scriptSessionId);  
        map.put("c0-scriptName", "dwrLieBiaoService");  
        map.put("c0-methodName", "getWorkflowList");  
        map.put("c0-id", "0");  
        map.put("c0-param0", "string:gwtype%3D'fw'");  
        map.put("c0-param1", "string:");  
        map.put("c0-param2", "string:admin");  
        map.put("c0-param3", "number:0");  
        map.put("c0-param4", "string:15");  
        map.put("c0-param5", "string:SENDTIME%20DESC");  
        map.put("batchId", "0");  
        dwrTest.setAction(action);  
        dwrTest.setMap(map);  
        dwrTest.setHeaders(headers);  
        html = dwrTest.execute();  
          
        System.out.println(html);  
    }  
      
    /** 
     * 初始化 
     */  
    public DwrTest() {  
        HttpParams params = new BasicHttpParams();  
        ConnManagerParams.setMaxTotalConnections(params, 100);  
        ConnManagerParams.setTimeout(params, 1000);  
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
        // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);  
        SchemeRegistry schemeRegistry = new SchemeRegistry();  
        schemeRegistry.register(  
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);  
          
        httpclient = new DefaultHttpClient(cm, params);  
        httpclient = new DefaultHttpClient();  
    }  
      
    /** 
     * 初始化 
     *  
     * @param action 
     * @param username 
     * @param passwork 
     * @param map 
     */  
    public DwrTest(String action,   
            String username, String passwork, Map<String, String> map) {  
  
        HttpParams params = new BasicHttpParams();  
        ConnManagerParams.setMaxTotalConnections(params, 100);  
        ConnManagerParams.setTimeout(params, 10000);  
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);  
        // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_0);  
        SchemeRegistry schemeRegistry = new SchemeRegistry();  
        schemeRegistry.register(  
                new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));  
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);  
          
        httpclient = new DefaultHttpClient(cm, params);  
//      httpclient = new DefaultHttpClient();  
        this.action = action;  
        this.username = username;  
        this.password = passwork;  
        if (map == null) {  
            map = new HashMap<String, String>();  
        }  
        this.map = map;  
    }  
      
    /** 
     * 初始化 
     *  
     * @param httpclient 
     */  
    public DwrTest(DefaultHttpClient httpclient) {  
        this.httpclient = httpclient;  
    }  
      
    /** 
     * 初始化 
     *  
     * @param httpclient 
     * @param username 
     * @param passwork 
     */  
    public DwrTest(DefaultHttpClient httpclient, String action,   
            String username, String passwork, Map<String, String> map) {  
        this.httpclient = httpclient;  
        this.action = action;  
        this.username = username;  
        this.password = passwork;  
        if (map == null) {  
            map = new HashMap<String, String>();  
        }  
        this.map = map;  
    }  
      
    /** 
     * 登录 
     *  
     * @param httpclient 
     * @return 
     * @throws Exception 
     */  
    public String login() throws Exception {  
        HttpPost httpost = new HttpPost(action);  // 初始化Post  
  
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();  // 构建参数  
        nvps.add(new BasicNameValuePair("username", username));  
        nvps.add(new BasicNameValuePair("password", password));  
        // 增加其他的参数  
        Set<String> set = map.keySet();  
        for (String string : set) {  
            nvps.add(new BasicNameValuePair(string, map.get(string)));  
        }  
          
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        httpost.setHeaders(headers);  
          
        HttpResponse response = httpclient.execute(httpost);  // 运行action  
        HttpEntity entity = response.getEntity();   // 获得实体  
  
        InputStream in = entity.getContent();  // 获得实体的内容  
        StringBuffer   out   =   new   StringBuffer();  
        byte[] b = new byte[4096];  
        for(int n; (n = in.read(b)) != -1;) {  
            out.append(new   String(b, 0, n));  
        }  
  
        if (entity != null) {  
            entity.consumeContent();  
        }  
          
//      取出页面上返回的LtpaToken Cookie值。  
        String html = out.toString();  
        String regEx = "(\"LtpaToken=(.+)\")";  
        String ltpaToken = null;  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(html);  
        while (m.find()) {  
            ltpaToken = m.group(1);  
            if (ltpaToken.length() > 11) {  
                ltpaToken = ltpaToken.substring(11);  
                ltpaToken = ltpaToken.substring(0, ltpaToken.lastIndexOf("\""));  
            }  
        }  
        BasicClientCookie cookie = new BasicClientCookie("LtpaToken", ltpaToken);  
        cookie.setDomain(".tj.unicom.local");  
        cookie.setPath("/");  
        CookieStore cookies = httpclient.getCookieStore();  
        cookies.addCookie(cookie);  
        httpclient.setCookieStore(cookies);  
          
        return html;  
    }  
      
    public String execute() throws Exception {  
        HttpPost httpost = new HttpPost(action);  // 初始化Post  
  
        List <NameValuePair> nvps = new ArrayList <NameValuePair>();  // 构建参数  
        // 增加其他的参数  
        Set<String> set = map.keySet();  
        for (String string : set) {  
            nvps.add(new BasicNameValuePair(string, map.get(string)));  
        }  
          
        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        httpost.setHeaders(headers);  
          
        HttpResponse response = httpclient.execute(httpost);  // 运行action  
        HttpEntity entity = response.getEntity();   // 获得实体  
  
        InputStream in = entity.getContent();  // 获得实体的内容  
        StringBuffer   out   =   new   StringBuffer();  
        byte[] b = new byte[4096];  
        for(int n; (n = in.read(b)) != -1;) {  
            out.append(new   String(b, 0, n));  
        }  
  
        if (entity != null) {  
            entity.consumeContent();  
        }  
          
        String html = out.toString();  
        return html;  
    }  
      
    @SuppressWarnings("unused")  
    private void print(InputStream in) throws Exception {  
        StringBuffer   out   =   new   StringBuffer();  
        byte[] b = new byte[4096];  
        for(int n; (n = in.read(b)) != -1;) {  
            out.append(new   String(b, 0, n));  
        }  
        System.out.println("-------------------------------------------");  
        System.out.println(out.toString());  
        System.out.println("-------------------------------------------");  
          
    }  
      
    @SuppressWarnings("unused")  
    private String toStr(InputStream in) throws Exception {  
        StringBuffer   out   =   new   StringBuffer();  
        byte[] b = new byte[4096];  
        for(int n; (n = in.read(b)) != -1;) {  
            out.append(new   String(b, 0, n));  
        }  
        String str = out.toString();  
        return str;  
    }  
  
    public String getAction() {  
        return action;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
      
    public Map<String, String> getMap() {  
        return map;  
    }  
  
    public void setAction(String action) {  
        this.action = action;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
      
    public void setMap(Map<String, String> map) {  
        this.map = map;  
    }  
  
    public DefaultHttpClient getHttpclient() {  
        return httpclient;  
    }  
  
    public Cookie getCookie() {  
        return cookie;  
    }  
  
    public void setHttpclient(DefaultHttpClient httpclient) {  
        this.httpclient = httpclient;  
    }  
  
    public void setCookie(Cookie cookie) {  
        this.cookie = cookie;  
    }  
      
    public void setHeaders(Header[] headers) {  
        this.headers = headers;  
    }  
      
    public Header[] getHeaders() {  
        return headers;  
    }  
}  