package cn.edu.zju.isst.party.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.zju.isst.api.service.ShowService;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;

@Controller
public class PartyShowController extends BaseController {
    @Autowired
    private ShowService showService;

    @RequestMapping("/shows.html")
    public String index(@ModelAttribute("user") LoggedUser user, Model model) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }

        model.addAttribute("shows", showService.retrieveForUser(user.getId()));
        model.addAttribute("title", "节目评分");
        return "shows.page";
    }

    @RequestMapping("/showForm.html")
    public String form(@RequestParam(value = "showId", required = false, defaultValue = "0") int showId, Model model,
            @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }

        Show show = null;
        if (showId == 0) {
            show = new Show();
        } else {
            show = showService.get(showId);
        }

        model.addAttribute("show", show);
        model.addAttribute("title", showId == 0 ? "添加节目" : "编辑节目");

        return "show-form.dialog";
    }

    @RequestMapping("/showVote.html")
    public String vote(@RequestParam(value = "showId") int showId, Model model,
            @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 0) {
            return "redirect:login.html";
        }
        
        Show show = showService.get(showId);
        
        if (null == show) {
            return "redirect:index.html";
        }
        
        model.addAttribute("show", show);
        model.addAttribute("hasVote", showService.hasVote(user.getId(), showId));
        model.addAttribute("title", show.getName());
        
        return "show-vote.dialog";
    }

    @RequestMapping(value = "/showForm", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder post(Show show, @ModelAttribute("user") LoggedUser user) {
        if (show.getName() == null || show.getName().equals("")) {
            return new ResultHolder("节目名称不能为空");
        }
        showService.save(show);
        return new ResultHolder(show.getId());
    }
}
