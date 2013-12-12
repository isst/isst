package cn.edu.zju.isst.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.edu.zju.isst.api.service.AppReleaseService;
import cn.edu.zju.isst.entity.AppRelease;

@Controller
public class AppReleaseController {
    @Autowired
    private AppReleaseService appReleaseSevice;
    
    @RequestMapping("/android/version")
    public @ResponseBody AppRelease getAndroidLatestVersion() {
        return appReleaseSevice.getLatestVersion(AppRelease.ANDROID);
    }
}
