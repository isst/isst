package cn.edu.zju.isst.party.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.User;

@Controller
public class PartyUserController extends BaseController {
    @Autowired
    private UserDao userDao;
    
    @RequestMapping("/login.html")
    public String login(@ModelAttribute("user") User user) {
        if (user.getId() > 0) {
            return "redirect:index.html";
        }
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void loginPost(HttpSession session, @RequestParam("id") int id) {
        session.setAttribute(USER_SESSION_KEY, userDao.getUserById(id));
    }
}
