package cn.edu.zju.isst.party.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.edu.zju.isst.api.service.SpittleServiceImpl;
import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.pushlet.PushingSpittle;
import cn.edu.zju.isst.pushlet.SpittleEventPullSource;

@Controller
public class PartyAdminController extends BaseController {
    @Autowired
    private ShowDao showDao;
 
    @RequestMapping(value="/admin.html", method=RequestMethod.GET)
    public String index(Model model, @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            model.addAttribute("shows", showDao.retrieve(2013));
            return "admin.page";
        }
        return "redirect:index.html";
    }
    
    @RequestMapping("/admin/getLotterySpittles.json")
    public @ResponseBody List<PushingSpittle> getLotterySpittles() {
        SpittleServiceImpl ssi = SpittleServiceImpl.getInstance();
        if (ssi != null) {
            return ssi.retrievePushingSpittles();
        }
        return new ArrayList<PushingSpittle>();
    }
    
    public @ResponseBody ResultHolder savePrize(@RequestParam("spittleId") int spittleId) {
        
        return null;
    }
    
    @RequestMapping(value = "/admin/pausePushing", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void pause() {
        SpittleEventPullSource.pausePushing();
    }
    
    @RequestMapping(value = "/admin/resumePushing", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void resume() {
        SpittleEventPullSource.resumePushing();
    }
}
