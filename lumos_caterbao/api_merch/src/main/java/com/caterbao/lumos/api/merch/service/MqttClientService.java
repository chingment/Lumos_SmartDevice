package com.caterbao.lumos.api.merch.service;

import com.caterbao.lumos.locals.common.JsonUtil;
import com.caterbao.lumos.locals.common.vo.FileVo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Service
@ConditionalOnProperty(value = "mqtt.enable", havingValue = "true")
public class MqttClientService {

    private static final Logger logger = LoggerFactory.getLogger(MqttClientService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private static MqttConnectOptions mMqttConnectOptions;
    private MqttClient mMqttClient;

    private static String subTopic="/+/+/user/update";//订阅主题
    @PostConstruct
    public void init(){
        buildMqttClient();
    }


    private final IMqttActionListener mqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            logger.info("连接成功");
            try {
                mMqttClient.subscribe(subTopic, 1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            logger.info("连接失败");
        }
    };

    private final MqttCallback mqttCallback = new MqttCallbackExtended() {  //回传
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            logger.info("连接成功："+reconnect+",serverURI:"+serverURI);
            try {
                if (mMqttClient != null) {
                    mMqttClient.subscribe(subTopic, 1);
                }
            }
            catch (Exception ex){
                logger.error("connectComplete",ex);
            }
        }

        @Override
        public void connectionLost(Throwable cause) {
            logger.info("连接断开");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {  // 接收的消息
            logger.info("接收信息："+topic);

            if(topic.contains("/user/update")) {
                String str_Payload = new String(message.getPayload());
                HashMap<String,Object> payload = JsonUtil.toObject(str_Payload, new TypeReference<HashMap<String,Object>>() {
                });

                String msgId=payload.get("id").toString();
                String method=payload.get("method").toString();
                String pms=JsonUtil.getJson(payload.get("params"));
                String deviceId=topic.split("/")[2];
                switch (method) {
                    case "msg_arrive":
                        msg_arrive(msgId);
                        break;
                    case "device_status":
                        device_status(deviceId, pms);
                        break;
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            logger.info("deliveryComplete：");
        }
    };

    private void buildMqttClient() {
        try {

            closeMqttClient();

            String servserUri="tcp://8.134.80.92:1883";
            String clientId="api_merch";
            String userName="admin";
            String password="public";
            mMqttClient = new MqttClient(servserUri, clientId, new MemoryPersistence());

            mMqttConnectOptions = new MqttConnectOptions();
            // 在重新启动和重新连接时记住状态
             mMqttConnectOptions.setCleanSession(true);
            // 设置连接的用户名
            mMqttConnectOptions.setUserName(userName);
            // 设置密码connect-onFailure-java
            mMqttConnectOptions.setPassword(password.toCharArray());
            // 设置超时时间，单位：秒
            //mMqttConnectOptions.setConnectionTimeout(10);
            // 心跳包发送间隔，单位：秒
            //mMqttConnectOptions.setKeepAliveInterval(20);
            mMqttConnectOptions.setAutomaticReconnect(true);
            mMqttClient.setCallback(mqttCallback);// 回调

            connectMqttClient();
        }
        catch (Exception ex){
            logger.error("buildMqttClient",ex);
        }

    }

    private synchronized void connectMqttClient() {
        if (mMqttConnectOptions == null)
            return;
        if (mMqttClient == null)
            return;
        if (mMqttClient.isConnected())
            return;

        try {
            mMqttClient.connect(mMqttConnectOptions);
            logger.info("连接中，ClientId：" + mMqttClient.getClientId());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void closeMqttClient(){
        logger.info("关闭连接");
        if (mMqttClient != null){
            try {
                logger.info("关闭连接.ClientId："+mMqttClient.getClientId());
                mMqttClient.close();
                mMqttClient.disconnect();
                mMqttClient = null;
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public void publish(String topic, String payload,int qos,boolean retained) {
        try {
            if (mMqttClient != null) {
                if (mMqttClient.isConnected()) {
                    mMqttClient.publish(topic, payload.getBytes(), qos, retained);
                }
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void msg_arrive(String msgId){

        redisTemplate.opsForValue().set("mqtt_msg:" + msgId, "1", 15, TimeUnit.SECONDS);

    }

    public void device_status(String deviceId,String pms){




    }


}