package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.config.SyncConfigProperties;
import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.server.dao.VeDeptDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.dao.VeEmpDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.entity.VeEmpTemp4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeEmpMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VeEmpTempMapper;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.*;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpByDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpWithDeptPosQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryDetailVO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpByDeptQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpWithDeptPosVO;
import cn.vetech.charge.cloud.demo.server.utils.DataCleanUtils;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VeEmpBusinessService {

    private static final Logger log = LoggerFactory.getLogger(VeEmpBusinessService.class);

    @Autowired
    private VeEmpDaoServiceImpl veEmpDaoService;

    @Autowired
    private VeDeptDaoServiceImpl veDeptDaoService;

    @Autowired
    private VeEmpMapper veEmpMapper;

    @Autowired
    private VeEmpTempMapper veEmpTempMapper;

    @Autowired
    private SyncConfigProperties syncConfigProperties;

    @Autowired
    private SyncCircuitBreaker syncCircuitBreaker;

    @Autowired
    private SyncAuditLogService syncAuditLogService;

    @Autowired
    private SyncMqNotificationService syncMqNotificationService;

    // ==================== 增 ====================
    @Transactional(rollbackFor = Exception.class)
    public void save(VeEmpSaveDTO dto) throws SystemException {
        VeEmp4849 exist = veEmpDaoService.selectByQybhAndGh(dto.getQybh(), dto.getGh());
        if (exist != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该员工已存在，请勿重复添加");
        }

        String employeeid = IdGenerator.getHexId();
        Date nowtime = new Date();

        VeEmp4849 entity = new VeEmp4849();
        entity.setId(employeeid);
        entity.setQybh(dto.getQybh());
        entity.setGh(dto.getGh());
        entity.setAccount(dto.getAccount());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setEnglishSurname(dto.getEnglishSurname());
        entity.setEnglishName(dto.getEnglishName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setBirthday(dto.getBirthday());
        entity.setAccountStatus(dto.getAccountStatus());
        entity.setVersionNo(dto.getVersionNo());
        entity.setCreatorId(dto.getCreatorId() != null ? dto.getCreatorId() : "me");
        entity.setDataSource("1"); // 标记为手工维护
        entity.setCreateTime(nowtime);
        entity.setUpdateTime(nowtime);

        veEmpDaoService.insertVeEmp(entity);
    }

    // ==================== 改 ====================
    @Transactional(rollbackFor = Exception.class)
    public void update(VeEmpUpdDTO dto) throws SystemException {
        String employeeid = dto.getId();

        VeEmp4849 exist = veEmpDaoService.selectById(employeeid);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "员工不存在");
        }

        Date nowtime = new Date();

        VeEmp4849 entity = new VeEmp4849();
        entity.setId(employeeid);
        entity.setQybh(dto.getQybh());
        entity.setGh(dto.getGh());
        entity.setAccount(dto.getAccount());
        entity.setPassword(dto.getPassword());
        entity.setName(dto.getName());
        entity.setEnglishSurname(dto.getEnglishSurname());
        entity.setEnglishName(dto.getEnglishName());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setGender(dto.getGender());
        entity.setBirthday(dto.getBirthday());
        entity.setAccountStatus(dto.getAccountStatus());
        entity.setVersionNo(dto.getVersionNo());
        entity.setCreatorId(dto.getCreatorId());
        entity.setDataSource(exist.getDataSource() != null ? exist.getDataSource() : "1");
        entity.setUpdateTime(nowtime);

        veEmpDaoService.updateVeEmp(entity);
    }

    // ==================== 50万员工批量/增量同步核心逻辑 (支持 500个一批) ====================

    /**
     * 员工数据同步引擎
     * @param qybh 企业编号
     * @param isIncremental 是否增量同步 (true: 增量, false: 全量)
     * @param batchSize 单批次处理条数（默认 500 个一批）
     * @return 产生的审计日志 URL
     */
    @Transactional(rollbackFor = Exception.class)
    public String syncEmpBatchData(String qybh, boolean isIncremental, int batchSize) {
        if (batchSize <= 0) {
            batchSize = syncConfigProperties.getPageSize(); // 从配置获取 500 个一批
        }
        String batchId = UUID.randomUUID().toString().replace("-", "");
        List<String> auditLogs = new ArrayList<>();
        auditLogs.add("【数据同步开启】批次ID: " + batchId + ", 企业编号: " + qybh + ", 增量同步: " + isIncremental + ", 批次大小: " + batchSize);

        try {
            // 1. 清理临时表历史残留
            veEmpTempMapper.clearTempTable(qybh);

            // 2. 增量点位获取
            Date lastSyncTime = null;
            if (isIncremental) {
                lastSyncTime = veEmpTempMapper.selectMaxUpdateTime(qybh);
                if (lastSyncTime == null) {
                    List<VeEmp4849> latestEmps = veEmpMapper.selectPage(
                            new Page<VeEmp4849>(1, 1),
                            new EntityWrapper<VeEmp4849>().eq("qybh", qybh).orderBy("update_time", false)
                    );
                    if (CollectionUtils.isNotEmpty(latestEmps)) {
                        lastSyncTime = latestEmps.get(0).getUpdateTime();
                    }
                }
                auditLogs.add("【增量同步断点点位】" + (lastSyncTime != null ? lastSyncTime : "无点位，全量同步"));
            }

            // 3. 校验网络接口，带网络重试与完整性计算
            int totalCount = fetchTotalCountWithRetry(qybh, lastSyncTime, auditLogs);
            int totalPages = (int) Math.ceil((double) totalCount / batchSize);
            auditLogs.add("【分页计算】总数据量: " + totalCount + " 条, 总页数: " + totalPages + " 页, 每批次: " + batchSize + " 条");

            int resignEmpCount = 0;
            int totalFetchedTempCount = 0;

            // 4. 按 500 个一批循环获取与处理
            for (int page = 1; page <= totalPages; page++) {
                // 带网络异常重试机制 (最多重试 3 次)
                List<VeEmp4849> pageDataList = fetchPageDataWithRetry(qybh, lastSyncTime, page, batchSize, auditLogs);
                if (CollectionUtils.isEmpty(pageDataList)) {
                    continue;
                }

                List<VeEmpTemp4849> tempBatch = new ArrayList<>();

                for (VeEmp4849 rawEmp : pageDataList) {
                    // 性别清洗兼容 (男/女 -> M/F)
                    String cleanedGender = DataCleanUtils.cleanGender(rawEmp.getGender(), auditLogs);
                    rawEmp.setGender(cleanedGender);

                    // 手机号清洗兼容 (去除 +86)
                    String cleanedPhone = DataCleanUtils.cleanPhone(rawEmp.getPhone(), auditLogs);
                    rawEmp.setPhone(cleanedPhone);

                    if ("0".equals(rawEmp.getAccountStatus())) {
                        resignEmpCount++;
                    }

                    VeEmpTemp4849 temp = new VeEmpTemp4849();
                    temp.setId(rawEmp.getId() != null ? rawEmp.getId() : IdGenerator.getHexId());
                    temp.setQybh(qybh);
                    temp.setGh(rawEmp.getGh());
                    temp.setAccount(rawEmp.getAccount());
                    temp.setPassword(rawEmp.getPassword());
                    temp.setName(rawEmp.getName());
                    temp.setEnglishSurname(rawEmp.getEnglishSurname());
                    temp.setEnglishName(rawEmp.getEnglishName());
                    temp.setPhone(rawEmp.getPhone());
                    temp.setEmail(rawEmp.getEmail());
                    temp.setAddress(rawEmp.getAddress());
                    temp.setGender(rawEmp.getGender());
                    temp.setBirthday(rawEmp.getBirthday());
                    temp.setAccountStatus(rawEmp.getAccountStatus());
                    temp.setVersionNo(rawEmp.getVersionNo() != null ? rawEmp.getVersionNo() : 1);
                    temp.setCreatorId("SYNC");
                    temp.setDataSource("2");
                    temp.setCreateTime(rawEmp.getCreateTime() != null ? rawEmp.getCreateTime() : new Date());
                    temp.setUpdateTime(rawEmp.getUpdateTime() != null ? rawEmp.getUpdateTime() : new Date());
                    tempBatch.add(temp);
                }

                // 500 个一批通过 MyBatis-Plus insertBatch 写入临时表
                if (CollectionUtils.isNotEmpty(tempBatch)) {
                    veEmpTempMapper.insertBatch(tempBatch);
                    totalFetchedTempCount += tempBatch.size();
                }

                // 显式内存释放 + 休眠 150ms 释放 CPU
                pageDataList.clear();
                tempBatch.clear();
                try {
                    Thread.sleep(syncConfigProperties.getPageSleepMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // 5. 校验数据完整性：同一批次的数据，获取完整了（写入临时表条数与总条数一致）才能进行后续合并！
            if (totalFetchedTempCount < totalCount) {
                String integrityErr = String.format("【数据完整性校验失败】预期条数: %d, 实际落临时表条数: %d，阻断向业务主表同步！", totalCount, totalFetchedTempCount);
                auditLogs.add(integrityErr);
                syncMqNotificationService.sendAlertMessage("SYNC_INTEGRITY_CHECK_FAILED", integrityErr);
                throw new RuntimeException(integrityErr);
            }

            // 6. 离职率熔断保护校验：取 min(1000, 10%)
            boolean isTriggeredCircuitBreaker = syncCircuitBreaker.checkResignCircuitBreaker(resignEmpCount, totalCount, auditLogs);

            // 7. 将临时表数据合并至正式业务表（保护手工维护 dataSource=1 的数据）
            mergeTempToBusinessTable(qybh, isTriggeredCircuitBreaker, auditLogs, batchSize);

            auditLogs.add("【数据同步完成】成功处理 " + totalFetchedTempCount + " 条数据，并通过完整性校验。");

        } catch (Exception e) {
            String errorMsg = "【数据同步中断】" + e.getMessage();
            log.error(errorMsg, e);
            auditLogs.add(errorMsg);
            syncMqNotificationService.sendAlertMessage("SYNC_EMP_EXCEPTION", errorMsg);
            throw new RuntimeException("员工同步失败: " + e.getMessage(), e);
        } finally {
            return syncAuditLogService.writeAndUploadAuditLog(batchId, auditLogs);
        }
    }

    /**
     * 将临时表数据合并写入正式业务表（保持手工维护数据不改变，查询按 200 条/批切片避开 Druid 告警，写入按 500 条/批落库）
     */
    private void mergeTempToBusinessTable(String qybh, boolean isTriggeredCircuitBreaker, List<String> auditLogs, int batchSize) {
        int queryPageSize = syncConfigProperties.getQueryPageSize(); // 200 条/批，防 Druid 告警
        int writeBatchSize = (batchSize > 0) ? batchSize : syncConfigProperties.getWriteBatchSize(); // 500 条/批，高效落库

        int totalTempCount = veEmpTempMapper.selectCount(new EntityWrapper<VeEmpTemp4849>().eq("qybh", qybh));
        if (totalTempCount <= 0) {
            return;
        }

        int totalPages = (int) Math.ceil((double) totalTempCount / queryPageSize);

        List<VeEmp4849> insertList = new ArrayList<>();
        List<VeEmp4849> updateList = new ArrayList<>();

        for (int page = 1; page <= totalPages; page++) {
            List<VeEmpTemp4849> tempEmps = veEmpTempMapper.selectPage(
                    new Page<VeEmpTemp4849>(page, queryPageSize),
                    new EntityWrapper<VeEmpTemp4849>().eq("qybh", qybh)
            );
            if (CollectionUtils.isEmpty(tempEmps)) {
                continue;
            }

            Set<String> batchGhs = tempEmps.stream()
                    .map(VeEmpTemp4849::getGh)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());

            Map<String, VeEmp4849> existGhMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(batchGhs)) {
                List<VeEmp4849> existEmps = veEmpMapper.selectList(
                        new EntityWrapper<VeEmp4849>().eq("qybh", qybh).in("gh", batchGhs)
                );
                if (CollectionUtils.isNotEmpty(existEmps)) {
                    for (VeEmp4849 exist : existEmps) {
                        existGhMap.put(exist.getGh(), exist);
                    }
                }
            }

            for (VeEmpTemp4849 temp : tempEmps) {
                VeEmp4849 exist = existGhMap.get(temp.getGh());

                if (exist == null) {
                    insertList.add(buildEmpFromTemp(temp));
                } else {
                    if (DataCleanUtils.isManualRecord(exist.getDataSource())) {
                        auditLogs.add("【跳过手工数据】工号: " + exist.getGh() + " (dataSource=1)");
                        continue;
                    }

                    VeEmp4849 updEmp = buildEmpFromTemp(temp);
                    updEmp.setId(exist.getId());

                    if (isTriggeredCircuitBreaker) {
                        updEmp.setAccountStatus(exist.getAccountStatus());
                    }
                    updateList.add(updEmp);
                }
            }

            // 当积累满/超过 500 条时，严格拆分为 500 条/批进行落库
            while (insertList.size() >= writeBatchSize) {
                List<VeEmp4849> batch = new ArrayList<>(insertList.subList(0, writeBatchSize));
                veEmpMapper.insertBatch(batch);
                insertList.subList(0, writeBatchSize).clear();
            }
            while (updateList.size() >= writeBatchSize) {
                List<VeEmp4849> batch = new ArrayList<>(updateList.subList(0, writeBatchSize));
                for (VeEmp4849 upd : batch) {
                    veEmpMapper.updateVeEmp(upd);
                }
                updateList.subList(0, writeBatchSize).clear();
            }

            try {
                Thread.sleep(syncConfigProperties.getPageSleepMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 刷新尾部剩余数据（仍按最多 500 条/批拆分提交）
        if (CollectionUtils.isNotEmpty(insertList)) {
            for (int i = 0; i < insertList.size(); i += writeBatchSize) {
                List<VeEmp4849> batch = insertList.subList(i, Math.min(i + writeBatchSize, insertList.size()));
                veEmpMapper.insertBatch(batch);
            }
            insertList.clear();
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            for (int i = 0; i < updateList.size(); i += writeBatchSize) {
                List<VeEmp4849> batch = updateList.subList(i, Math.min(i + writeBatchSize, updateList.size()));
                for (VeEmp4849 upd : batch) {
                    veEmpMapper.updateVeEmp(upd);
                }
            }
            updateList.clear();
        }
    }

    private VeEmp4849 buildEmpFromTemp(VeEmpTemp4849 temp) {
        VeEmp4849 emp = new VeEmp4849();
        emp.setId(temp.getId());
        emp.setQybh(temp.getQybh());
        emp.setGh(temp.getGh());
        emp.setAccount(temp.getAccount());
        emp.setPassword(temp.getPassword());
        emp.setName(temp.getName());
        emp.setEnglishSurname(temp.getEnglishSurname());
        emp.setEnglishName(temp.getEnglishName());
        emp.setPhone(temp.getPhone());
        emp.setEmail(temp.getEmail());
        emp.setAddress(temp.getAddress());
        emp.setGender(temp.getGender());
        emp.setBirthday(temp.getBirthday());
        emp.setAccountStatus(temp.getAccountStatus());
        emp.setVersionNo(temp.getVersionNo());
        emp.setCreatorId(temp.getCreatorId());
        emp.setDataSource("2");
        emp.setCreateTime(temp.getCreateTime());
        emp.setUpdateTime(temp.getUpdateTime());
        return emp;
    }

    // 网络调用重试机制获取总条数 (最多重试 3 次，指数退避)
    private int fetchTotalCountWithRetry(String qybh, Date lastSyncTime, List<String> auditLogs) throws Exception {
        int maxRetries = syncConfigProperties.getMaxRetryAttempts();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return mockFetchTotalCountFromClient(qybh, lastSyncTime);
            } catch (Exception e) {
                auditLogs.add(String.format("【网络异常重试】获取总条数第 %d 次失败: %s", attempt, e.getMessage()));
                if (attempt == maxRetries) {
                    throw new RuntimeException("调用客户接口网络不通/异常，重试 " + maxRetries + " 次后放弃: " + e.getMessage(), e);
                }
                Thread.sleep(attempt * 1000L); // 指数退避重试
            }
        }
        return 0;
    }

    // 网络调用重试机制获取分页数据 (最多重试 3 次)
    private List<VeEmp4849> fetchPageDataWithRetry(String qybh, Date lastSyncTime, int page, int pageSize, List<String> auditLogs) throws Exception {
        int maxRetries = syncConfigProperties.getMaxRetryAttempts();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return mockFetchPageDataFromClient(qybh, lastSyncTime, page, pageSize, auditLogs);
            } catch (Exception e) {
                auditLogs.add(String.format("【网络异常重试】获取第 %d 页数据第 %d 次失败: %s", page, attempt, e.getMessage()));
                if (attempt == maxRetries) {
                    throw new RuntimeException("调用客户接口第 " + page + " 页异常，重试 " + maxRetries + " 次后放弃", e);
                }
                Thread.sleep(attempt * 1000L);
            }
        }
        return new ArrayList<>();
    }

    private int mockFetchTotalCountFromClient(String qybh, Date lastSyncTime) {
        return 5000;
    }

    private List<VeEmp4849> mockFetchPageDataFromClient(String qybh, Date lastSyncTime, int page, int pageSize, List<String> auditLogs) {
        List<VeEmp4849> list = new ArrayList<>();
        int count = Math.min(pageSize, 5000 - (page - 1) * pageSize);
        for (int i = 0; i < count; i++) {
            VeEmp4849 emp = new VeEmp4849();
            String gh = "EMP_" + String.format("%05d", (page - 1) * pageSize + i + 1);
            emp.setId(IdGenerator.getHexId());
            emp.setQybh(qybh);
            emp.setGh(gh);
            emp.setName("测试员工" + i);
            emp.setGender(i % 2 == 0 ? "男" : "女");
            emp.setPhone("+861380000" + String.format("%04d", i));
            emp.setAccountStatus("1");
            emp.setCreateTime(new Date());
            emp.setUpdateTime(new Date());
            list.add(emp);
        }
        return list;
    }

    // ==================== 删 ====================
    public void delete(String id) throws SystemException {
        VeEmp4849 exist = veEmpDaoService.selectById(id);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "员工不存在");
        }
        veEmpDaoService.deleteById(id);
    }

    // ==================== 改（状态/密码） ====================
    public void updateState(VeEmpStatusUpdDTO dto) {
        VeEmp4849 entity = new VeEmp4849();
        entity.setId(dto.getId());
        entity.setAccountStatus(dto.getAccountStatus());
        entity.setUpdateTime(new Date());
        veEmpDaoService.updateEmpById(entity);
    }

    public void updPassword(VeEmpUpdPwdDTO dto) throws SystemException {
        VeEmp4849 exist = veEmpDaoService.selectById(dto.getId());
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "员工不存在");
        }
        VeEmp4849 updEntity = new VeEmp4849();
        updEntity.setId(dto.getId());
        updEntity.setPassword(dto.getPassword());
        updEntity.setUpdateTime(new Date());
        veEmpDaoService.updateEmpById(updEntity);
    }

    // ==================== 分页查 ====================
    public List<VeEmpQryVO> queryList(Page<VeEmpQryVO> page, VeEmpQryDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            return new ArrayList<>();
        }
        return veEmpDaoService.queryList(page, dto);
    }

    // ==================== 查询详情 ====================
    public VeEmpQryDetailVO queryOne(VeEmpQryDetailDTO dto) throws SystemException {
        String empId = dto.getId();

        VeEmp4849 veEmp4849 = veEmpDaoService.selectById(empId);
        if (Objects.isNull(veEmp4849)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "员工不存在");
        }

        return BeanMapper.map(veEmp4849, VeEmpQryDetailVO.class);
    }

    // ==================== 多表 ====================
    public List<EmpWithDeptPosVO> queryEmpListWithDeptAndPos(Page<EmpWithDeptPosVO> page, EmpWithDeptPosQryDTO dto) throws SystemException {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "企业编号不能为空");
        }
        return veEmpDaoService.queryEmpListWithDeptAndPos(page, dto);
    }

    public List<EmpByDeptQryVO> queryEmpListByDept(Page<EmpByDeptQryVO> page, EmpByDeptQryDTO dto) throws SystemException {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "企业编号不能为空");
        }
        if (StringUtils.isBlank(dto.getDeptId())) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "部门id不能为空");
        }
        VeDept4849 dept = veDeptDaoService.selectByQybhAndDeptId(dto.getQybh(), dto.getDeptId());
        if (Objects.isNull(dept)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "部门不存在");
        }
        return veEmpDaoService.queryEmpListByDept(page, dto);
    }
}