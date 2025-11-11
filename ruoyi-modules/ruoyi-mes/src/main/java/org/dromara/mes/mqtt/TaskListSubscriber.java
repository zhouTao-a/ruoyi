package org.dromara.mes.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.nio.charset.StandardCharsets;

public class TaskListSubscriber {

    private static final String BROKER_IP = "223.83.224.81";
    private static final int BROKER_PORT = 56789;
    private static final String USERNAME = "HJ_Mes";
    private static final String PASSWORD = "HJ!.2025";

    public static void main(String[] args) {
        String brokerUrl = String.format("tcp://%s:%d", BROKER_IP, BROKER_PORT);
        String clientId = "HJ_MES_TEST";
        String topic = "task/lists";  // 要订阅的主题

        try {
            MqttClient client = new MqttClient(brokerUrl, clientId);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            options.setCleanSession(true);
            options.setKeepAliveInterval(30); // 秒

            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            // 设置消息回调
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("❌ 连接丢失: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    System.out.println("📥 收到任务列表消息:");
                    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                    System.out.println("主题: " + topic);
                    System.out.println("消息内容:\n" + payload);

                    // 这里你可以解析 JSON，比如用 fastjson2 / Jackson
                    // 示例:
                    // List<TaskData> tasks = JSON.parseArray(payload, TaskData.class);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // 对订阅端没用
                }
            });

            System.out.println("连接到 Broker: " + brokerUrl);
            client.connect(options);
            System.out.println("✅ 已连接，clientId=" + clientId);

            // 订阅主题
            client.subscribe(topic, 1);
            System.out.println("✅ 已订阅主题: " + topic);

            // 阻塞等待消息
            System.out.println("等待接收任务列表消息...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
