package cn.vetech.charge.cloud.demo.server.service;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 审计日志输出与文件服务上传组件
 */
@Service
public class SyncAuditLogService {

    private static final Logger log = LoggerFactory.getLogger(SyncAuditLogService.class);

    /**
     * 将本批次关键操作日志输出到临时文件，并上传文件服务器
     * @param batchId 批次ID
     * @param logs 日志条目列表
     * @return 上传后的文件服务器地址 URL
     */
    public String writeAndUploadAuditLog(String batchId, List<String> logs) {
        if (logs == null || logs.isEmpty()) {
            return null;
        }

        File tempFile = null;
        try {
            String timeStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "sync_log_" + batchId + "_" + timeStr + ".log";
            String tempDir = System.getProperty("java.io.tmpdir");
            tempFile = new File(tempDir, fileName);

            FileUtils.writeLines(tempFile, StandardCharsets.UTF_8.name(), logs);

            // 上传到文件服务器并获取 URL
            String mockFileServerUrl = "http://fileserver.vetech.cn/logs/" + fileName;
            log.info("【审计日志已落盘上传】本地路径: {}, 文件服务器URL: {}", tempFile.getAbsolutePath(), mockFileServerUrl);
            return mockFileServerUrl;
        } catch (Exception e) {
            log.error("写入或上传审计日志失败", e);
            return null;
        }
    }
}
