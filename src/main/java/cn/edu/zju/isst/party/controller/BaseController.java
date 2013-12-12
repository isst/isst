package cn.edu.zju.isst.party.controller;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import cn.edu.zju.isst.entity.LoggedUser;

public class BaseController {
    protected final static String USER_SESSION_KEY = "current-user";
    
    @ModelAttribute
    public void populateCurrentUser(Model model, HttpSession session) {
        LoggedUser user = (LoggedUser) session.getAttribute(USER_SESSION_KEY);
        if (null == user) {
            user = new LoggedUser();
        }
        
        model.addAttribute("user", user);
    }
}
