package cn.edu.zju.isst.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.zju.isst.dao.ShowDao;
import cn.edu.zju.isst.entity.ResultHolder;
import cn.edu.zju.isst.entity.Show;

@Service
public class ShowServiceImpl implements ShowService {
    @Autowired
    private ShowDao showDao;
    
    private static int defaultYear = 2013;

    @Override
    public List<Show> retrieve() {
        return showDao.retrieve(defaultYear);
    }

    @Override
    public ResultHolder vote(int userId, int showId) {
        // TODO Auto-generated method stub
        return null;
    }
}
