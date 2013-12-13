package cn.edu.zju.isst.bccs;

import java.util.List;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.BindInfo;
import com.baidu.yun.channel.model.PushBroadcastMessageRequest;
import com.baidu.yun.channel.model.PushBroadcastMessageResponse;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.channel.model.QueryBindListRequest;
import com.baidu.yun.channel.model.QueryBindListResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;

public class BccsApi {
    private final static String apiKey = "";
    private final static String secretKey = "";

    public static void queryBindList(String _userId) {

        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                // TODO Auto-generated method stub
                System.out.println(event.getMessage());
            }
        });

        try {
            // 4. 创建请求类对象
            // 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            QueryBindListRequest request = new QueryBindListRequest();
            request.setUserId(_userId);

            // 5. 调用queryBindList接口
            QueryBindListResponse response = channelClient.queryBindList(request);

            // 6. 对返回的结果对象进行操作
            List<BindInfo> bindInfos = response.getBinds();
            for (BindInfo bindInfo : bindInfos) {
                long channelId = bindInfo.getChannelId();
                String userId = bindInfo.getUserId();
                int status = bindInfo.getBindStatus();
                System.out.println("channel_id:" + channelId + ", user_id: " + userId + ", status: " + status);

                String bindName = bindInfo.getBindName();
                long bindTime = bindInfo.getBindTime();
                String deviceId = bindInfo.getDeviceId();
                int deviceType = bindInfo.getDeviceType();
                long timestamp = bindInfo.getOnlineTimestamp();
                long expire = bindInfo.getOnlineExpires();

                System.out.println("bind_name:" + bindName + "\t" + "bind_time:" + bindTime);
                System.out.println("device_type:" + deviceType + "\tdeviceId" + deviceId);
                System.out.println(String.format("timestamp: %d, expire: %d", timestamp, expire));
            }

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
                    e.getErrorCode(), e.getErrorMsg()));
        }

    }

    public static boolean pushBroadcastMessage(String message) {

        /*
         * @brief 推送广播消息(消息类型为透传，由开发方应用自己来解析消息内容) message_type = 0 (默认为0)
         */

        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            PushBroadcastMessageRequest request = new PushBroadcastMessageRequest();
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android 4:ios 5:wp

            request.setMessage(message);
            // 若要通知，
            // request.setMessageType(1);
            // request.setMessage("{\"title\":\"Notify_title_danbo\",\"description\":\"Notify_description_content\"}");

            // 5. 调用pushMessage接口
            PushBroadcastMessageResponse response = channelClient.pushBroadcastMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());
            return true;

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
            return false;
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
                    e.getErrorCode(), e.getErrorMsg()));
            return false;
        }

    }

    public static boolean pushNotification(String message, long channelId, String userId) {

        /*
         * @brief 推送单播通知(Android Push SDK拦截并解析) message_type = 1 (默认为0)
         */

        // 1. 设置developer平台的ApiKey/SecretKey
        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

        // 2. 创建BaiduChannelClient对象实例
        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

        // 3. 若要了解交互细节，请注册YunLogHandler类
        channelClient.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent event) {
                System.out.println(event.getMessage());
            }
        });

        try {

            // 4. 创建请求类对象
            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
            request.setDeviceType(3); // device_type => 1: web 2: pc 3:android 4:ios 5:wp
            request.setChannelId(channelId);
            request.setUserId(userId);

            request.setMessageType(1);
            request.setMessage(message);

            // 5. 调用pushMessage接口
            PushUnicastMessageResponse response = channelClient.pushUnicastMessage(request);

            // 6. 认证推送成功
            System.out.println("push amount : " + response.getSuccessAmount());
            return true;

        } catch (ChannelClientException e) {
            // 处理客户端错误异常
            e.printStackTrace();
            return false;
        } catch (ChannelServerException e) {
            // 处理服务端错误异常
            System.out.println(String.format("request_id: %d, error_code: %d, error_message: %s", e.getRequestId(),
                    e.getErrorCode(), e.getErrorMsg()));
            return false;
        }
        
    }
}
