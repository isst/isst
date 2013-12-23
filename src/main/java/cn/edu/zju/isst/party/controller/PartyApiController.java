package cn.edu.zju.isst.party.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import cn.edu.zju.isst.config.ApiConfig;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.util.StringUtils;

@Controller
public class PartyApiController extends BaseController {
    @Qualifier("restTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private UserDao userDao;
    
    @SuppressWarnings("unchecked")
    @RequestMapping("/api")
    public @ResponseBody String api(@ModelAttribute("user") LoggedUser user, HttpServletResponse response, HttpServletRequest request) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, String> data = new HashMap<String, String>(); 
        Map<String, String[]> rawData = (Map<String, String[]>)request.getParameterMap();
        
        boolean isLogin = false;
        boolean isUpdateNickname = false;
        String path = null;
        String method = null;
        for (Map.Entry<String, String[]> entry : rawData.entrySet()) {
            String key = entry.getKey();
            if (key.equals("_method")) {
                method = entry.getValue()[0];
            } else if (key.equals("_path")) {
                path = entry.getValue()[0];
            } else {
                data.put(key, entry.getValue()[0]);
            }
        }

        if ("/users/validation".equals(path)) {
            isLogin = true;
        } else if (user.getId() == 0) {
            return new JSONObject(new ResultHolder("未登录")).toString();
        } else if ("/users/{userId}".equals(path) && "PUT".equals(method)) {
            isUpdateNickname = true;
        }
        
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(data.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Entry<String, String> o1, Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String expire = String.valueOf(System.currentTimeMillis()/1000);
        StringBuilder sb = new StringBuilder(ApiConfig.APP_SECRET_KEY);
        sb.append(expire);
        for (Map.Entry<String, String> entry : list) {
            sb.append(entry.getValue());
        }
        
        String token = StringUtils.md5(sb.toString());
        data.put("token", token);
        data.put("expire", expire);
        
        String patternString = "\\{([a-zA-Z0-9_]+)\\}";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(path);
     
        StringBuffer buffer = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(buffer, data.get(matcher.group(1)));
            data.remove(matcher.group(1));
        }
        matcher.appendTail(buffer);
        path = buffer.toString();
        
        if (null == method) {
            method = request.getMethod();
        }
        
        String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + "/isst/api" + path;
        if (method.equals("GET")) {
            if (!data.isEmpty()) {
                StringBuilder urlBuilder = new StringBuilder(url);
                if (url.indexOf('?') == -1) {
                    urlBuilder.append("?");
                }
                int i=0;
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    if (i>0) {
                        urlBuilder.append("&");
                    }
                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                    i++;
                }
                url = urlBuilder.toString();
            }
            return restTemplate.getForObject(url, String.class);
        } else {
            data.put("_method", method);
            LinkedMultiValueMap<String, String> resp = new LinkedMultiValueMap<String, String>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                resp.add(entry.getKey(), entry.getValue());
            }
            if (isLogin) {
                String result = restTemplate.postForObject(url, resp, String.class);
                JSONObject json = new JSONObject(result);
                int code = json.getInt("code");
                if (code > 0) {
                    request.getSession().setAttribute(USER_SESSION_KEY, new LoggedUser(userDao.getUserById(code)));
                }
                return result;
            } else if (isUpdateNickname) {
                String result = restTemplate.postForObject(url, resp, String.class);
                request.getSession().setAttribute(USER_SESSION_KEY, new LoggedUser(userDao.getUserById(Integer.valueOf(request.getParameter("userId")))));
                return result;
            } else {
                return restTemplate.postForObject(url, resp, String.class);
            }
        }
    }
}
