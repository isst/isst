package cn.edu.zju.isst.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.zju.isst.api.service.ShowService;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.UserShowVote;

@Controller
public class ShowController {
	@Autowired
    private ShowService showService;
	
	@RequestMapping(value="/users/{userId}/shows", method = RequestMethod.GET)
    public @ResponseBody
	List<UserShowVote> retrieve(@PathVariable("userId") int userId) {
		return showService.retrieveForUser(userId);
	}
	
	@RequestMapping(value = "/users/{userId}/shows/{showId}/votes",method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder vote(@PathVariable("userId") int userId, @PathVariable("showId") int showId) {
		return showService.vote(userId, showId);
	}
}
