package cn.vetech.charge.cloud.demo.server.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 数据清洗与格式兼容工具类
 */
public class DataCleanUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{11}$");

    /**
     * 性别清洗兼容
     * @param rawGender 原始性别字符
     * @param auditLogs 审计日志列表
     * @return 标准化性别 'M' / 'F' / null
     */
    public static String cleanGender(String rawGender, List<String> auditLogs) {
        if (StringUtils.isBlank(rawGender)) {
            return null;
        }
        String trimStr = rawGender.trim();
        if ("男".equals(trimStr) || "M".equalsIgnoreCase(trimStr)) {
            return "M";
        }
        if ("女".equals(trimStr) || "F".equalsIgnoreCase(trimStr)) {
            return "F";
        }
        // 非标准非法值记录错误日志并提醒
        if (auditLogs != null) {
            auditLogs.add("【数据清洗提醒】性别返回非标准值: '" + rawGender + "'");
        }
        return null;
    }

    /**
     * 手机号清洗兼容
     * @param rawPhone 原始手机号
     * @param auditLogs 审计日志列表
     * @return 清洗后的11位手机号
     */
    public static String cleanPhone(String rawPhone, List<String> auditLogs) {
        if (StringUtils.isBlank(rawPhone)) {
            return rawPhone;
        }
        String cleaned = rawPhone.trim();
        // 兼容去除 +86 或 86 前缀
        if (cleaned.startsWith("+86")) {
            cleaned = cleaned.substring(3).trim();
        } else if (cleaned.startsWith("86-")) {
            cleaned = cleaned.substring(3).trim();
        } else if (cleaned.startsWith("+86-")) {
            cleaned = cleaned.substring(4).trim();
        }

        // 位数校验
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            if (auditLogs != null) {
                auditLogs.add("【数据清洗提醒】手机号位数或格式错误: '" + rawPhone + "' (清洗后: '" + cleaned + "')");
            }
        }
        return cleaned;
    }

    /**
     * 判断是否为手工维护的记录（dataSource = "1"）
     */
    public static boolean isManualRecord(String dataSource) {
        return "1".equals(dataSource);
    }
}
