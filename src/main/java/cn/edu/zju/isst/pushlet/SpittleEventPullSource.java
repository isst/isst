package cn.edu.zju.isst.pushlet;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import cn.edu.zju.isst.dao.SpittleDao;
import cn.edu.zju.isst.entity.PushingSpittle;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;

public class SpittleEventPullSource extends EventPullSource implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static boolean isPushingActived = true;
    
    private static SpittleEventPullSource instance;
    
    public SpittleEventPullSource() {
        super();
        instance = this;
    }

    public static boolean isPushingActived() {
        return isPushingActived;
    }
    
    public static void pausePushing() {
        if (instance != null) {
            instance.passivate();
            isPushingActived = false;
        }
    }
    
    public static void resumePushing() {
        if (instance != null) {
            isPushingActived = true;
            instance.activate();
        }
    }
    
    private static void parsePushingSpittle(PushingSpittle pushingSpittle) {
        pushingSpittle.setContent(encodeString(pushingSpittle.getContent()));
        pushingSpittle.setNickname(encodeString(pushingSpittle.getNickname()));
    }
    
    @Override
    protected long getSleepTime() {
        return 7000;
    }

    @Override
    protected Event pullEvent() {
        Event event = Event.createDataEvent("/party/spittles");
        SpittleDao spittleDao = SpittleDao.getInstance();
        if (spittleDao != null) {
            PushingSpittle pushingSpittle = spittleDao.getPushingSpittle();
            if (pushingSpittle != null) {
                parsePushingSpittle(pushingSpittle);
                JSONObject json = new JSONObject(pushingSpittle);
                event.setField("spittle", json.toString());
            }
        }
        return event;
    }
    
    private static String encodeString(String str) {
        try {
            return URLEncoder.encode(str,"UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
}
