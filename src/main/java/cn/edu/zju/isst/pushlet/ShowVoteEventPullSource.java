package cn.edu.zju.isst.pushlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import cn.edu.zju.isst.dao.ShowDao;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;

public class ShowVoteEventPullSource  extends EventPullSource implements Serializable {
    private static final long serialVersionUID = 1L;
    private static ShowVoteEventPullSource instance;
    private Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    
    public ShowVoteEventPullSource() {
        super();
        instance = this;
        map.put(1, 1);
        map.put(3, 5);
    }
    
    public static void startVoting() {
        if (instance != null) {
            instance.activate();
        }
    }
    
    @Override
    protected long getSleepTime() {
        return 5000;
    }

    @Override
    protected Event pullEvent() {
        this.passivate();
        
        Event event = Event.createDataEvent("/party/votes");
        ShowDao showDao = ShowDao.getInstance();
        if (null != showDao) {
            JSONObject json = new JSONObject(showDao.statisticalVote());
            event.setField("votes", json.toString());
        }
        
        return event;
    }
}
