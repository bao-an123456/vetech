package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.config.SyncConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据同步离职率熔断保护组件
 */
@Component
public class SyncCircuitBreaker {

    @Autowired
    private SyncConfigProperties syncConfigProperties;

    @Autowired
    private SyncMqNotificationService syncMqNotificationService;

    /**
     * 校验离职员工数量是否触发熔断保护
     * @param resignCount 本批次/全量中的离职员工数
     * @param totalCount 总员工数
     * @param auditLogs 日志采集列表
     * @return true 如果触发熔断（阻断离职状态更新）；false 正常
     */
    public boolean checkResignCircuitBreaker(int resignCount, int totalCount, List<String> auditLogs) {
        if (totalCount <= 0) {
            return false;
        }

        int countLimit = syncConfigProperties.getResignThresholdCount(); // 默认 1000
        int ratioLimit = (int) Math.ceil(totalCount * syncConfigProperties.getResignThresholdRatio()); // 默认 10%
        int effectiveThreshold = Math.min(countLimit, ratioLimit); // 取最小值

        if (resignCount > effectiveThreshold) {
            String msg = String.format("【熔断阻断】离职员工数 [%d] 超过熔断阈值 [%d] (限制规则: min(1000, 10%%))，疑似服务端异常，已阻断离职状态更新！",
                    resignCount, effectiveThreshold);
            if (auditLogs != null) {
                auditLogs.add(msg);
            }
            // 发送胜意 MQ 消息通知
            syncMqNotificationService.sendAlertMessage("SYNC_RESIGN_CIRCUIT_BREAKER_ALERT", msg);
            return true;
        }
        return false;
    }
}
