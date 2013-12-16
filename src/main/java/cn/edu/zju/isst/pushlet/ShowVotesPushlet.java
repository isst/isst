package cn.edu.zju.isst.pushlet;

import nl.justobjects.pushlet.core.Dispatcher;
import nl.justobjects.pushlet.core.Event;

public class ShowVotesPushlet {
    
    public static void push() {
        Event event = Event.createDataEvent("/party/showVotes");
        event.setField("key1", "Unicast msg");
        Dispatcher.getInstance().unicast(event, "piero");
    }
}
