package cn.edu.zju.isst.party.controller;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.edu.zju.isst.entity.LoggedUser;

@Controller
public class PartyUserController extends BaseController {

    @RequestMapping("/login.html")
    public String login(@ModelAttribute("user") LoggedUser user, Model model,
            @RequestParam(value = "returnUrl", required = false, defaultValue = "") String returnUrl) {
        if (user.getId() > 0) {
            return "redirect:index.html";
        }
        model.addAttribute("title", "登录");
        model.addAttribute("returnUrl", returnUrl);
        return "login.page";
    }

    @RequestMapping(value = "/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpSession session) {
        session.removeAttribute(USER_SESSION_KEY);
    }

    @RequestMapping(value = "/nickname.html")
    public String loginPost(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }

        model.addAttribute("title", "修改昵称");
        return "nickname.dialog";
    }
}
