package cn.edu.zju.isst.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.zju.isst.api.service.SpittleService;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.UserSpittle;

@Controller
@RequestMapping("/users/{userId}/spittles")
public class SpittleController {
    @Autowired
    private SpittleService spittleService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<UserSpittle> retrieve(@PathVariable("userId") int userId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "id", required = false, defaultValue = "0") int id) {
        return spittleService.retrieve(userId, "post_time", page, pageSize, id);
    }

    @RequestMapping(value = "/likes")
    public @ResponseBody
    List<UserSpittle> retrieveLikes(@PathVariable("userId") int userId,
            @RequestParam(value = "isLike", required = false, defaultValue = "0") int isLike,
            @RequestParam(value = "count", required = false, defaultValue = "20") int count) {
        return spittleService.retrieve(userId, isLike == 0 ? "dislikes" : "likes", 0, count, 0);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder post(@PathVariable("userId") int userId, @RequestParam("content") String content) {
        return spittleService.post(userId, content);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResultHolder delete(@PathVariable("userId") int userId, @PathVariable("id") int spittleId) {
        return spittleService.delete(userId, spittleId);
    }

    @RequestMapping(value = "/{id}/likes", method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder like(@PathVariable("userId") int userId, @PathVariable("id") int spittleId,
            @RequestParam("isLike") int isLike) {
        return spittleService.like(userId, spittleId, isLike);
    }
}
