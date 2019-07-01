package com.dhht.nana.data;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Tyhj
 * @date 2019/7/1
 */

public class RequestData {


    public static final String APP_KEY = "obpSTQhdz2SBQG2H";
    public static final String APP_ID = "2117866454";


    String app_id;

    String time_stamp;

    String nonce_str;

    String sign;

    String session;

    String question;


    public RequestData(String session, String question) {
        this.session = session;
        this.question = question;
        app_id = APP_ID;
        time_stamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);
        nonce_str = String.valueOf(System.currentTimeMillis()).substring(5, 12);
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", app_id);
        map.put("time_stamp", time_stamp);
        map.put("nonce_str", nonce_str);
        map.put("session", session);
        map.put("question", question);
        try {
            sign = getSignature(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getSignature(Map<String, Object> params) throws IOException {
        Map<String, Object> sortedParams = new TreeMap<>(params);
        Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();
        StringBuilder baseString = new StringBuilder();
        for (Map.Entry<String, Object> param : entrys) {
            if (param.getValue() != null && !"".equals(param.getKey().trim()) &&
                    !"sign".equals(param.getKey().trim()) && !"".equals(param.getValue())) {
                baseString.append(param.getKey().trim()).append("=")
                        .append(URLEncoder.encode(param.getValue().toString(), "UTF-8")).append("&");
            }
        }
        if (baseString.length() > 0) {
            baseString.deleteCharAt(baseString.length() - 1).append("&app_key=")
                    .append(APP_KEY);
        }


        try {
            String sign = MD5(baseString.toString());
            System.out.println("sign:" + sign.toUpperCase());
            return sign.toUpperCase();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }


    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    public static String getAppKey() {
        return APP_KEY;
    }

    public static String getAppId() {
        return APP_ID;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
