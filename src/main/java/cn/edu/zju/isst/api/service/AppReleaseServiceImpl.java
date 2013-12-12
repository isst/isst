package cn.edu.zju.isst.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.AppReleaseDao;
import cn.edu.zju.isst.entity.AppRelease;

@Service
public class AppReleaseServiceImpl implements AppReleaseService {
    @Autowired
    private AppReleaseDao appReleaseDao;
    
    @Override
    public AppRelease getLatestVersion(int type) {
        return appReleaseDao.getLatestVersion(type);
    }
}
