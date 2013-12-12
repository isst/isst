package cn.edu.zju.isst.party.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.zju.isst.entity.LoggedUser;

@Controller
public class PartyHomeController extends BaseController {
    @RequestMapping({ "", "/", "/index.html" })
    public String index(@ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }
        return "spittles.page";
    }
}
