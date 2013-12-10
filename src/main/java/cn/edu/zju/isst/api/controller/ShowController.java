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
import cn.edu.zju.isst.entity.Show;

@Controller
public class ShowController {
	@Autowired
    private ShowService showService;
	
	@RequestMapping(value="/shows", method = RequestMethod.GET)
    public @ResponseBody
	List<Show> retrieve() {
		return showService.retrieve();
	}
	
	@RequestMapping(value = "/users/{userId}/shows/{id}",method = RequestMethod.POST)
    public @ResponseBody
    ResultHolder vote(@PathVariable("userId") int userId, @PathVariable("id") int showId) {
		return showService.vote(userId, showId);
	}
}
