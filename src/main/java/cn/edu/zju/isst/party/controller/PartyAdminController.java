package cn.edu.zju.isst.party.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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

import cn.edu.zju.isst.bccs.BccsApi;
import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.entity.LoggedUser;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;
import cn.edu.zju.isst.entity.PushingSpittle;
import cn.edu.zju.isst.pushlet.SpittleEventPullSource;

@Controller
public class PartyAdminController extends BaseController {
    @Autowired
    private ShowDao showDao;
    @Autowired
    private SpittleDao spittleDao;

    @RequestMapping(value = "/admin.html", method = RequestMethod.GET)
    public String index(Model model, @ModelAttribute("user") LoggedUser user) {
        if (user.getId() == 1) {
            model.addAttribute("shows", showDao.retrieve(2013));
            return "admin.page";
        }
        return "redirect:index.html";
    }

    @RequestMapping(value = "/votes.html")
    public String votes(Model model) {
        List<String> showNames = new ArrayList<String>();
        List<Integer> showNameMapper = new ArrayList<Integer>();
        for (Show show : showDao.retrieve()) {
            showNames.add(show.getName());
            showNameMapper.add(show.getId());
        }

        model.addAttribute("showNames", new JSONArray(showNames));
        model.addAttribute("showNameMapper", new JSONArray(showNameMapper));
        model.addAttribute("voteData", new JSONObject(showDao.statisticalVote()));

        return "votes.html";
    }
    
    @RequestMapping(value = "/screen.html")
    public String screen() {
        return "screen.html";
    }

    @RequestMapping("/admin/getLotterySpittles.json")
    public @ResponseBody
    List<PushingSpittle> getLotterySpittles() {
        return spittleDao.retrievePushingSpittles();
    }

    public @ResponseBody
    ResultHolder savePrize(@RequestParam("spittleId") int spittleId) {

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

    @RequestMapping(value = "/admin/pushMessage", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder pushMessage(@ModelAttribute("user") LoggedUser user, @RequestParam("title") String title,
            @RequestParam("description") String description) {
        if (user.getId() != 1) {
            return new ResultHolder("无权限");
        }
        int amount = BccsApi.pushAndroidBroadcastMessage("version", title, description);
        return new ResultHolder(amount);
    }
}
