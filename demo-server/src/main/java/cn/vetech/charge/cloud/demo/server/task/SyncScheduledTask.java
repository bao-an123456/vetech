package cn.vetech.charge.cloud.demo.server.task;

import cn.vetech.charge.cloud.demo.server.service.VeDeptBusinessService;
import cn.vetech.charge.cloud.demo.server.service.VeEmpBusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 自动同步定时任务组件 (实现每天凌晨 2 点自动增量同步)
 */
@Component
@EnableScheduling
public class SyncScheduledTask {

    private static final Logger log = LoggerFactory.getLogger(SyncScheduledTask.class);

    @Autowired
    private VeEmpBusinessService veEmpBusinessService;

    @Autowired
    private VeDeptBusinessService veDeptBusinessService;

    /**
     * 每天凌晨 2 点自动同步员工与部门增量数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void autoSyncTask() {
        log.info("【定时任务触发】每天凌晨 2 点数据自动增量同步开启...");
        String defaultQybh = "VETECH";
        try {
            // 1. 部门增量同步
            String deptLogUrl = veDeptBusinessService.syncDeptBatchData(defaultQybh, true);
            log.info("【定时部门同步完成】审计日志URL: {}", deptLogUrl);

            // 2. 员工增量同步
            String empLogUrl = veEmpBusinessService.syncEmpBatchData(defaultQybh, true, 500);
            log.info("【定时员工同步完成】审计日志URL: {}", empLogUrl);

        } catch (Exception e) {
            log.error("【定时同步任务失败】", e);
        }
    }
}
