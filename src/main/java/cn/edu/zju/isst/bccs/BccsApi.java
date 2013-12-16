package cn.edu.zju.isst.bccs;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;

public class BccsApi {
    protected final static String apiKey = "PqDQfrucX3ubvW7fm0M23gWu";
    protected final static String secretKey = "Drpun8Glo38STs3ayCtxbkEd2nzVLRu3";
    protected final static ChannelKeyPair channekKeyPair = new ChannelKeyPair(apiKey, secretKey);
    protected final static BaiduChannelClient channelClient = new BaiduChannelClient(channekKeyPair);
    
    public static int pushBroadcastMessage(PushBroadcastMessageRequest request) {
        try {
            PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
            return response.getSuccessAmount();
        } catch (ChannelClientException e) {
            return 0;
        } catch (ChannelServerException e) {
            return 0;
        }
    }
    
    public static int pushAndroidBroadcastMessage(String messageKey, String message) {
        PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
        request.setMsgKey(messageKey);
        request.setDeviceType(3);
        request.setMessage(message);
        return pushBroadcastMessage(request);
    }
    
    public static int pushAndroidBroadcastMessage(String messageKey, String title, String description) {
        PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
        request.setMsgKey(messageKey);
        request.setDeviceType(3);
        request.setMessageType(1);
        request.setMessage("{\"title\":\"" + title + "\",\"description\":\"" + description + "\"}");
        return pushBroadcastMessage(request);
    }
}
