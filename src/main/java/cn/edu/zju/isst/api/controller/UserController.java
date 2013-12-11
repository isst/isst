package cn.edu.zju.isst.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.zju.isst.api.service.UserService;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.User;

@Controller
@RequestMapping("/users")
public class UserController {
	@Autowired
    private UserService userService;
	
	@RequestMapping(value="/validation", method = RequestMethod.POST)
    public @ResponseBody
	ResultHolder validate(@RequestParam("name") String name, @RequestParam("password") String password) {
		return userService.validate(name, password);
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User get(@PathVariable("id") int userId) {
		return userService.get(userId);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody
    ResultHolder updateNickname(@PathVariable("id") int userId, @RequestParam("nickname") String nickname) {
		return userService.updateNickname(userId, nickname);
	}
      
}
