package cn.edu.zju.isst.pushlet;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.json.JSONObject;

import nl.justobjects.pushlet.core.Event;
import nl.justobjects.pushlet.core.EventPullSource;

public class SpittleEventPullSource extends EventPullSource implements Serializable {
    private static final long serialVersionUID = 1L;
    private static ConcurrentLinkedQueue<PushingSpittle> jumpQueue = new ConcurrentLinkedQueue<PushingSpittle>();
    private static ConcurrentLinkedQueue<PushingSpittle> normalQueue = new ConcurrentLinkedQueue<PushingSpittle>();
    
    private static SpittleEventPullSource instance;
    
    public SpittleEventPullSource() {
        super();
        instance = this;
    }
    
    public static void pausePushing() {
        if (instance != null) {
            instance.passivate();
        }
    }
    
    public static void resumePushing() {
        if (instance != null) {
            instance.activate();
        }
    }
    
    public static void addQueue(PushingSpittle pushingSpittle) {
        if (pushingSpittle != null) {
            parsePushingSpittle(pushingSpittle);
            normalQueue.add(pushingSpittle);
        }
    }
    
    public static void addQueue(List<PushingSpittle> pushingSpittles) {
        for (PushingSpittle pushingSpittle : pushingSpittles) {
            parsePushingSpittle(pushingSpittle);
        }
        normalQueue.addAll(pushingSpittles);
    }
    
    public static int getQueueSize() {
        return normalQueue.size();
    }
    
    public static void resetQueue() {
        normalQueue.clear();
    }
    
    public static void jumpQueue(PushingSpittle pushingSpittle) {
        if (pushingSpittle != null) {
            parsePushingSpittle(pushingSpittle);
            jumpQueue.add(pushingSpittle);
        }
    }
    
    private static void parsePushingSpittle(PushingSpittle pushingSpittle) {
        pushingSpittle.setContent(encodeString(pushingSpittle.getContent()));
        pushingSpittle.setNickname(encodeString(pushingSpittle.getNickname()));
    }
    
    @Override
    protected long getSleepTime() {
        return 6000;
    }

    @Override
    protected Event pullEvent() {
        Event event = Event.createDataEvent("/party/spittles");
        PushingSpittle spittle = jumpQueue.poll();
        if (spittle == null) {
            spittle = normalQueue.poll();
        }
        
        if (spittle != null) {
            JSONObject json = new JSONObject(spittle);
            event.setField("spittle", json.toString());
            normalQueue.add(spittle);
        }
        
        return event;
    }
    
    private static String encodeString(String str) {
        try {
            return URLEncoder.encode(str,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }
}
