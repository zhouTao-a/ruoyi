package org.dromara.mes.mqtt;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MqttPublisherDemo {

    // 根据你提供的信息修改
    private static final String BROKER_IP = "223.83.224.81";
    private static final int BROKER_PORT = 56789;
    private static final String PREFIX = "HJ_";         // 前缀
    private static final String USERNAME = "HJ_Mes";
    private static final String PASSWORD = "HJ!.2025";

    public static void main(String[] args) {
        String brokerUrl = String.format("tcp://%s:%d", BROKER_IP, BROKER_PORT);
        // clientId 随机，避免冲突
        String clientId = "java-producer-" + UUID.randomUUID();
        // 示例 topic（你可以改为 PREFIX + "设备ID/rtdvalue/report" 等）
        String topic = PREFIX + "test"; // 发布到 HJ_test

        // 示例 payload（JSON）
        String payload = "{"
                + "\"deviceId\":\"dev-001\","
                + "\"ts\": " + System.currentTimeMillis() + ","
                + "\"value\": 123.45,"
                + "\"status\":\"OK\""
                + "}";

        // QoS: 0,1,2 可选，测试推荐 1
        int qos = 1;

        MqttClient client = null;
        try {
            client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setAutomaticReconnect(true); // 自动重连
            options.setCleanSession(true); // 测试时通常 true，保持会话可设 false
            // 可选: 设置遗嘱消息 (LWT)
            String willTopic = topic + "/lwt";
            String willPayload = clientId + " unexpectedly disconnected";
            options.setWill(willTopic, willPayload.getBytes(StandardCharsets.UTF_8), 1, false);

            // 可选: callback 监听交互（这里主要用于打印）
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("连接丢失: " + cause);
                }

                @Override
                public void messageArrived(String topic, org.eclipse.paho.client.mqttv3.MqttMessage message) {
                    // 我们这里仅发布，所以通常不会触发
                    System.out.println("收到消息: topic=" + topic + ", payload=" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    try {
                        System.out.println("发送完成, topics: " + String.join(",", token.getTopics()));
                    } catch (Exception e) {
                        System.out.println("发送完成 (无法获取 topic): " + e.getMessage());
                    }
                }
            });

            System.out.println("尝试连接到 broker: " + brokerUrl);
            client.connect(options);
            System.out.println("已连接. clientId=" + clientId);

            // 发布单条消息
            MqttMessage msg = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            msg.setQos(qos);
            msg.setRetained(false); // 是否保留，测试通常 false
            client.publish(topic, msg);
            System.out.println("已发布到 topic=" + topic + ", payload=" + payload);

            // 示例：连续发送多条（模拟生产），每隔1秒发送5条
            for (int i = 1; i <= 5; i++) {
                String p = "{"
                        + "\"deviceId\":\"dev-001\","
                        + "\"seq\":" + i + ","
                        + "\"ts\":" + System.currentTimeMillis() + ","
                        + "\"value\":" + (100 + Math.random() * 50)
                        + "}";
                MqttMessage m = new MqttMessage(p.getBytes(StandardCharsets.UTF_8));
                m.setQos(qos);
                m.setRetained(false);
                String t = PREFIX + "dev-001/rtdvalue/report"; // 更真实的 topic 示例
                client.publish(t, m);
                System.out.println("Published -> topic: " + t + ", payload: " + p);
                Thread.sleep(1000);
            }

            // 发布完成，可以选择断开或保持连接
            Thread.sleep(500); // 等待回调
            client.disconnect();
            System.out.println("已断开连接.");

        } catch (MqttException me) {
            System.err.println("MQTT异常: reason=" + me.getReasonCode()
                    + ", msg=" + me.getMessage()
                    + ", loc=" + me.getLocalizedMessage()
                    + ", cause=" + me.getCause());
            me.printStackTrace();
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            if (client != null) {
                try {
                    if (client.isConnected()) client.disconnect();
                    client.close();
                } catch (MqttException e) {
                    // ignore
                }
            }
        }
    }
}
