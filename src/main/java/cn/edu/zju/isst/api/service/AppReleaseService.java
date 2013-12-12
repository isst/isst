package cn.edu.zju.isst.api.service;

import cn.edu.zju.isst.entity.AppRelease;

public interface AppReleaseService {
    AppRelease getLatestVersion(int type);
}
