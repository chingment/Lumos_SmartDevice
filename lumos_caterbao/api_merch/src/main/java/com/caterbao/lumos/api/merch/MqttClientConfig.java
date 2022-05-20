package com.caterbao.lumos.api.merch;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 两种实现方式，eclipse.paho可实时订阅，integration-mqtt在初始化阶段即完成订阅
 * mqtt消息处理配置
 * 属性文件中设置了key的值value：
 * ConditionalOnProperty设置了havingValue：value=havingvalue则匹配，若不等则不匹配
 * ConditionalOnproperty没有设置havingValue：value不等于false则匹配，若为false，则不匹配
 *
 * 属性文件中没有设置key：
 * ConditionalOnProperty中matchingIfMissing：true匹配，false不匹配
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enable", havingValue = "true")
@ConfigurationProperties(prefix = "mqtt")
public class MqttClientConfig {
    private String url;
    private String clientid;
    private String username;
    private String password;
    private String topic;
    private int timeout;
    private int keepalive;

    private MqttClient client;

    public MqttClient getClient() {
        return this.client;
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(int keepalive) {
        this.keepalive = keepalive;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
