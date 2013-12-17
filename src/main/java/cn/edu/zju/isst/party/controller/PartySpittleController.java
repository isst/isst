package cn.edu.zju.isst.party.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.edu.zju.isst.entity.LoggedUser;

@Controller
public class PartySpittleController extends BaseController {
    
    @RequestMapping("/spittles.html")
    public String index(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }
        
        model.addAttribute("title", "我要吐槽");
        
        return "spittles.page";
    }
}
