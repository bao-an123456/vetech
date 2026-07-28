package cn.vetech.charge.cloud.demo.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 数据同步 MQ 消息通知服务
 */
@Service
public class SyncMqNotificationService {

    private static final Logger log = LoggerFactory.getLogger(SyncMqNotificationService.class);

    /**
     * 发送胜意 MQ 异常报警消息
     * @param topic 消息 Topic
     * @param alertMessage 报警详情
     */
    public void sendAlertMessage(String topic, String alertMessage) {
        log.error("[MQ 异常提醒] Topic: {}, Payload: {}", topic, alertMessage);
        // 实际集成 MQ 时调用胜意 MQ 工具发送消息
    }
}
