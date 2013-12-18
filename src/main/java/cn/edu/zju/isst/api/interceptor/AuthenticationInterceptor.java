package cn.edu.zju.isst.api.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.edu.zju.isst.config.ApiConfig;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.util.StringUtils;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    @SuppressWarnings("unchecked")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> data = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); 
        Map<String, String[]> rawData = (Map<String, String[]>)request.getParameterMap();

        String token = null;
        String expire = null;
        for (Map.Entry<String, String[]> entry : rawData.entrySet()) {
            String key = entry.getKey();
            if (key.equals("_method")) {
                //ignored
            } else if (key.equals("token")) {
                token = entry.getValue()[0];
            } else if (key.equals("expire")) {
                expire = entry.getValue()[0];
            } else {
                data.put(key, entry.getValue()[0]);
            }
        }
        
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(data.entrySet());
        
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        
        StringBuilder sb = new StringBuilder(ApiConfig.APP_SECRET_KEY);
        sb.append(expire);
        for (Map.Entry<String, String> entry : list) {
            sb.append(entry.getValue());
        }
        
        String expectedToken = StringUtils.md5(sb.toString());

        if (expectedToken.equals(token)) {
            if (expire == null || System.currentTimeMillis()/1000 - Long.valueOf(expire).longValue() > 600) {
                responseError(response, "认证失效");
                return false;
            }
            return true;
        }
        
        responseError(response, "认证失败");
        
        return false;
    }
    
    private void responseError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        pw.append(new JSONObject(new ResultHolder(message)).toString());
    }
}
