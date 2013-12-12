package cn.edu.zju.isst.party.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.dao.UserDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.Spittle;
import cn.edu.zju.isst.entity.User;
import cn.edu.zju.isst.pushlet.PushingSpittle;
import cn.edu.zju.isst.pushlet.SpittleEventPullSource;

@Controller
public class PartyAdminController extends BaseController {
    @Autowired
    private SpittleDao spittleDao;
    @Autowired
    private UserDao userDao;
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
    
    @RequestMapping("/admin/lotterySpittles")
    public List<Spittle> getLotterySpittles() {
        return spittleDao.retrieve();
    }
    
    @RequestMapping("/admin/pushAllSpittles")
    public @ResponseBody Map<String, Object> pushAllSpittles() {
        List<PushingSpittle> pushingSpittles = new ArrayList<PushingSpittle>();
        for (Spittle spittle : spittleDao.retrieve()) {
            User user = userDao.getUserById(spittle.getUserId());
            if (user != null) {
                pushingSpittles.add(new PushingSpittle(user, spittle));
            }
        }

        SpittleEventPullSource.resetQueue();
        SpittleEventPullSource.addQueue(pushingSpittles);
        
        return getSpittleQueueSize();
    }
    
    @RequestMapping("/admin/spittleQueueSize")
    public @ResponseBody Map<String, Object> getSpittleQueueSize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("size", SpittleEventPullSource.getQueueSize());
        
        return map;
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
