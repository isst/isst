package cn.edu.zju.isst.bccs;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class BccsApi {
    private final static String apiKey = "PqDQfrucX3ubvW7fm0M23gWu";
    private final static String secretKey = "Drpun8Glo38STs3ayCtxbkEd2nzVLRu3";

    public static void main(String[] args) {
        pushBroadcastMessage("测试", "百度云推送功能");
    }

    public static boolean pushBroadcastMessage(String title, String content) {
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {
            PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
            request.setDeviceType(3);
            request.setMessageType(1);
            request.setMessage("{\"title\":\"" + title + "\",\"description\":\"" + content + "\"}");
            PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);
            System.out.println("push amount : " + response.getSuccessAmount());
            return true;

        } catch (ChannelClientException e) {
            e.printStackTrace();
            return false;
        } catch (ChannelServerException e) {
            return false;
        }

    }

}
