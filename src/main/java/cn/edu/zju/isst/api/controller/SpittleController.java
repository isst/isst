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
import cn.edu.zju.isst.entity.Spittle;

@Controller
@RequestMapping("/users/{userId}/spittles")
public class SpittleController {
    @Autowired
    private SpittleService spittleService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<Spittle> retrieve(@PathVariable("userId") int userId,
            @RequestParam(value = "order", required = false, defaultValue = "post_time") String order,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        return spittleService.retrieve(userId, order, page, pageSize);
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
            @RequestParam("like") int isLike) {
        return spittleService.like(userId, spittleId, isLike);
    }
}
