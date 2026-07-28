package cn.vetech.charge.cloud.demo.common.config;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 数据同步配置参数
 */
@Data
@Component
public class SyncConfigProperties {

    /**
     * 离职人数阈值（绝对值）
     */
    private int resignThresholdCount = 1000;

    /**
     * 离职人数比例阈值（如0.10表示10%）
     */
    private double resignThresholdRatio = 0.10;

    /**
     * 每页数据处理完成后的休眠时间（毫秒），用于释放CPU
     */
    private long pageSleepMs = 150L;

    /**
     * 分页查询/批量处理大小（固定 500 个一批）
     */
    private int pageSize = 500;

    /**
     * 网络接口失败重试最大次数
     */
    private int maxRetryAttempts = 3;

    /**
     * 核心线程池大小
     */
    private int corePoolSize = 5;

    /**
     * 最大线程池大小
     */
    private int maxPoolSize = 10;

    /**
     * 队列容量
     */
    private int queueCapacity = 100;
}
