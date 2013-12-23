package cn.edu.zju.isst.party.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.UserSpittle;

@Controller
public class PartySpittleController extends BaseController {
    @Autowired
    private SpittleDao spittleDao;

    @RequestMapping("/spittles.html")
    public String index(@RequestParam(value = "id", required = false, defaultValue = "0") int id,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }

        List<UserSpittle> userSpittles = spittleDao.retrieve(user.getId(), "post_time", page, pageSize, id);

        model.addAttribute("spittles", userSpittles);
        model.addAttribute("title", "我要吐槽");

        return "spittles.page";
    }
}
