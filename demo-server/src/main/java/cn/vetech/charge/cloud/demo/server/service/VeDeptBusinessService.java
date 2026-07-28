package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.config.SyncConfigProperties;
import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.server.dao.VeDeptDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.entity.VeDeptTemp4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeDeptMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VeDeptTempMapper;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptSaveDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptUpdDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.DeptTreeQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.dept.VeDeptQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.DeptTreeQryVO;
import cn.vetech.charge.cloud.demo.server.utils.DataCleanUtils;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
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
public class VeDeptBusinessService {

    private static final Logger log = LoggerFactory.getLogger(VeDeptBusinessService.class);

    @Autowired
    private VeDeptDaoServiceImpl veDeptDaoService;

    @Autowired
    private VeDeptMapper veDeptMapper;

    @Autowired
    private VeDeptTempMapper veDeptTempMapper;

    @Autowired
    private SyncConfigProperties syncConfigProperties;

    @Autowired
    private SyncAuditLogService syncAuditLogService;

    @Autowired
    private SyncMqNotificationService syncMqNotificationService;

    // ==================== 增 ====================
    @Transactional(rollbackFor = Exception.class)
    public void save(VeDeptSaveDTO dto) throws SystemException {
        VeDept4849 exist = veDeptDaoService.selectByQybhAndDeptId(dto.getQybh(), dto.getDeptId());
        if (exist != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该部门已存在，请勿重复添加");
        }
        VeDept4849 exist2 = veDeptDaoService.selectByQybhAndBh(dto.getQybh(), dto.getBh());
        if (exist2 != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该部门编号已存在，请勿重复添加");
        }
        validateParentExists(dto.getParentId());
        Date nowtime = new Date();
        String id = IdGenerator.getHexId();

        VeDept4849 entity = new VeDept4849();
        entity.setId(id);
        entity.setDeptId(dto.getDeptId());
        entity.setQybh(dto.getQybh());
        entity.setBh(dto.getBh());
        entity.setShortName(dto.getShortName());
        entity.setParentId(dto.getParentId());
        entity.setDetailAddress(dto.getDetailAddress());
        entity.setStatus(dto.getStatus());
        entity.setCreatorId("me");
        entity.setDataSource("1"); // 手工维护标记
        entity.setCreateTime(nowtime);
        entity.setUpdateTime(nowtime);

        buildDeptPath(entity);

        veDeptDaoService.insertVeDept(entity);
    }

    // ==================== 5万部门批量/增量同步核心逻辑 (支持 500个一批) ====================

    @Transactional(rollbackFor = Exception.class)
    public String syncDeptBatchData(String qybh, boolean isIncremental) {
        int batchSize = syncConfigProperties.getPageSize(); // 从配置获取 500 个一批
        String batchId = UUID.randomUUID().toString().replace("-", "");
        List<String> auditLogs = new ArrayList<>();
        auditLogs.add("【部门数据同步开启】批次ID: " + batchId + ", 企业编号: " + qybh + ", 增量同步: " + isIncremental);

        try {
            // 1. 清理临时表历史记录
            veDeptTempMapper.clearTempTable(qybh);

            // 2. 增量点位获取
            Date lastSyncTime = null;
            if (isIncremental) {
                lastSyncTime = veDeptTempMapper.selectMaxUpdateTime(qybh);
                if (lastSyncTime == null) {
                    List<VeDept4849> latestDepts = veDeptMapper.selectPage(
                            new Page<VeDept4849>(1, 1),
                            new EntityWrapper<VeDept4849>().eq("qybh", qybh).orderBy("update_time", false)
                    );
                    if (CollectionUtils.isNotEmpty(latestDepts)) {
                        lastSyncTime = latestDepts.get(0).getUpdateTime();
                    }
                }
            }

            // 3. 网络接口带重试机制获取总条数
            int totalCount = fetchDeptTotalCountWithRetry(qybh, lastSyncTime, auditLogs);
            int totalPages = (int) Math.ceil((double) totalCount / batchSize);
            int fetchedTempCount = 0;

            for (int page = 1; page <= totalPages; page++) {
                List<VeDept4849> pageDataList = fetchDeptPageDataWithRetry(qybh, lastSyncTime, page, batchSize, auditLogs);
                if (CollectionUtils.isEmpty(pageDataList)) {
                    continue;
                }

                List<VeDeptTemp4849> tempBatch = new ArrayList<>();
                for (VeDept4849 dept : pageDataList) {
                    VeDeptTemp4849 temp = new VeDeptTemp4849();
                    temp.setId(dept.getId() != null ? dept.getId() : IdGenerator.getHexId());
                    temp.setDeptId(dept.getDeptId());
                    temp.setQybh(qybh);
                    temp.setBh(dept.getBh());
                    temp.setShortName(dept.getShortName());
                    temp.setParentId(dept.getParentId());
                    temp.setDetailAddress(dept.getDetailAddress());
                    temp.setStatus(dept.getStatus());
                    temp.setCreatorId("SYNC");
                    temp.setDataSource("2");
                    temp.setCreateTime(dept.getCreateTime() != null ? dept.getCreateTime() : new Date());
                    temp.setUpdateTime(dept.getUpdateTime() != null ? dept.getUpdateTime() : new Date());
                    temp.setDeptIdPath(dept.getDeptIdPath());
                    temp.setDeptNamePath(dept.getDeptNamePath());
                    tempBatch.add(temp);
                }

                // 500 个一批批量写入临时表
                if (CollectionUtils.isNotEmpty(tempBatch)) {
                    veDeptTempMapper.insertBatch(tempBatch);
                    fetchedTempCount += tempBatch.size();
                }

                // 资源释放
                pageDataList.clear();
                tempBatch.clear();
                try {
                    Thread.sleep(syncConfigProperties.getPageSleepMs());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // 4. 数据完整性校验：必须整批获取完整才合并
            if (fetchedTempCount < totalCount) {
                String integrityErr = String.format("【部门数据完整性校验失败】预期: %d, 实际落临时表: %d", totalCount, fetchedTempCount);
                auditLogs.add(integrityErr);
                syncMqNotificationService.sendAlertMessage("SYNC_DEPT_INTEGRITY_FAILED", integrityErr);
                throw new RuntimeException(integrityErr);
            }

            // 5. 从临时表合并到正式业务表（保护手工数据）
            mergeTempDeptToBusinessTable(qybh, auditLogs, batchSize);
            auditLogs.add("【部门数据同步完成】成功合并处理 " + fetchedTempCount + " 条部门数据");

        } catch (Exception e) {
            String errorMsg = "【部门同步异常中断】" + e.getMessage();
            log.error(errorMsg, e);
            auditLogs.add(errorMsg);
            syncMqNotificationService.sendAlertMessage("SYNC_DEPT_EXCEPTION", errorMsg);
            throw new RuntimeException("部门同步失败: " + e.getMessage(), e);
        } finally {
            return syncAuditLogService.writeAndUploadAuditLog(batchId, auditLogs);
        }
    }

    private void mergeTempDeptToBusinessTable(String qybh, List<String> auditLogs, int batchSize) {
        int queryPageSize = syncConfigProperties.getQueryPageSize(); // 200 条/批，防止 Druid 告警
        int writeBatchSize = (batchSize > 0) ? batchSize : syncConfigProperties.getWriteBatchSize(); // 500 条/批落库

        int totalTempCount = veDeptTempMapper.selectCount(new EntityWrapper<VeDeptTemp4849>().eq("qybh", qybh));
        if (totalTempCount <= 0) {
            return;
        }

        int totalPages = (int) Math.ceil((double) totalTempCount / queryPageSize);

        List<VeDept4849> insertList = new ArrayList<>();
        List<VeDept4849> updateList = new ArrayList<>();

        for (int page = 1; page <= totalPages; page++) {
            List<VeDeptTemp4849> tempDepts = veDeptTempMapper.selectPage(
                    new Page<VeDeptTemp4849>(page, queryPageSize),
                    new EntityWrapper<VeDeptTemp4849>().eq("qybh", qybh)
            );
            if (CollectionUtils.isEmpty(tempDepts)) {
                continue;
            }

            Set<String> batchBhs = tempDepts.stream()
                    .map(VeDeptTemp4849::getBh)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());

            Map<String, VeDept4849> existBhMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(batchBhs)) {
                List<VeDept4849> existDepts = veDeptMapper.selectList(
                        new EntityWrapper<VeDept4849>().eq("qybh", qybh).in("bh", batchBhs)
                );
                if (CollectionUtils.isNotEmpty(existDepts)) {
                    for (VeDept4849 exist : existDepts) {
                        existBhMap.put(exist.getBh(), exist);
                    }
                }
            }

            for (VeDeptTemp4849 temp : tempDepts) {
                VeDept4849 exist = existBhMap.get(temp.getBh());
                if (exist == null) {
                    insertList.add(buildDeptFromTemp(temp));
                } else {
                    if (DataCleanUtils.isManualRecord(exist.getDataSource())) {
                        auditLogs.add("【跳过手工维护部门】部门编号: " + exist.getBh() + " (dataSource=1)");
                        continue;
                    }
                    VeDept4849 updDept = buildDeptFromTemp(temp);
                    updDept.setId(exist.getId());
                    updateList.add(updDept);
                }
            }

            while (insertList.size() >= writeBatchSize) {
                List<VeDept4849> batch = new ArrayList<>(insertList.subList(0, writeBatchSize));
                veDeptMapper.insertBatch(batch);
                insertList.subList(0, writeBatchSize).clear();
            }
            while (updateList.size() >= writeBatchSize) {
                List<VeDept4849> batch = new ArrayList<>(updateList.subList(0, writeBatchSize));
                for (VeDept4849 upd : batch) {
                    veDeptMapper.updateVeDept(upd);
                }
                updateList.subList(0, writeBatchSize).clear();
            }

            try {
                Thread.sleep(syncConfigProperties.getPageSleepMs());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (CollectionUtils.isNotEmpty(insertList)) {
            for (int i = 0; i < insertList.size(); i += writeBatchSize) {
                List<VeDept4849> batch = insertList.subList(i, Math.min(i + writeBatchSize, insertList.size()));
                veDeptMapper.insertBatch(batch);
            }
            insertList.clear();
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            for (int i = 0; i < updateList.size(); i += writeBatchSize) {
                List<VeDept4849> batch = updateList.subList(i, Math.min(i + writeBatchSize, updateList.size()));
                for (VeDept4849 upd : batch) {
                    veDeptMapper.updateVeDept(upd);
                }
            }
            updateList.clear();
        }
    }

    private VeDept4849 buildDeptFromTemp(VeDeptTemp4849 temp) {
        VeDept4849 dept = new VeDept4849();
        dept.setId(temp.getId());
        dept.setDeptId(temp.getDeptId());
        dept.setQybh(temp.getQybh());
        dept.setBh(temp.getBh());
        dept.setShortName(temp.getShortName());
        dept.setParentId(temp.getParentId());
        dept.setDetailAddress(temp.getDetailAddress());
        dept.setStatus(temp.getStatus());
        dept.setCreatorId(temp.getCreatorId());
        dept.setCreateTime(temp.getCreateTime());
        dept.setUpdateTime(temp.getUpdateTime());
        dept.setDeptIdPath(temp.getDeptIdPath());
        dept.setDeptNamePath(temp.getDeptNamePath());
        dept.setDataSource("2");
        return dept;
    }

    private int fetchDeptTotalCountWithRetry(String qybh, Date lastSyncTime, List<String> auditLogs) throws Exception {
        int maxRetries = syncConfigProperties.getMaxRetryAttempts();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return mockFetchDeptTotalCount(qybh, lastSyncTime);
            } catch (Exception e) {
                auditLogs.add(String.format("【部门网络异常重试】获取总条数第 %d 次失败", attempt));
                if (attempt == maxRetries) {
                    throw new RuntimeException("调用部门接口失败，已达到重试上限 " + maxRetries + " 次", e);
                }
                Thread.sleep(attempt * 1000L);
            }
        }
        return 0;
    }

    private List<VeDept4849> fetchDeptPageDataWithRetry(String qybh, Date lastSyncTime, int page, int pageSize, List<String> auditLogs) throws Exception {
        int maxRetries = syncConfigProperties.getMaxRetryAttempts();
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                return mockFetchDeptPageData(qybh, lastSyncTime, page, pageSize);
            } catch (Exception e) {
                auditLogs.add(String.format("【部门网络异常重试】获取第 %d 页数据第 %d 次失败", page, attempt));
                if (attempt == maxRetries) {
                    throw new RuntimeException("获取部门第 " + page + " 页数据异常，重试放弃", e);
                }
                Thread.sleep(attempt * 1000L);
            }
        }
        return new ArrayList<>();
    }

    private int mockFetchDeptTotalCount(String qybh, Date lastSyncTime) {
        return 1000;
    }

    private List<VeDept4849> mockFetchDeptPageData(String qybh, Date lastSyncTime, int page, int pageSize) {
        List<VeDept4849> list = new ArrayList<>();
        int count = Math.min(pageSize, 1000 - (page - 1) * pageSize);
        for (int i = 0; i < count; i++) {
            VeDept4849 dept = new VeDept4849();
            dept.setId(IdGenerator.getHexId());
            dept.setDeptId("100" + i);
            dept.setQybh(qybh);
            dept.setBh("DEPT_" + String.format("%04d", (page - 1) * pageSize + i + 1));
            dept.setShortName("部门_" + i);
            dept.setParentId("none");
            dept.setStatus("1");
            dept.setCreateTime(new Date());
            dept.setUpdateTime(new Date());
            list.add(dept);
        }
        return list;
    }

    // ==================== 删（级联删除子部门）====================
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws SystemException {
        VeDept4849 exist = veDeptDaoService.selectById(id);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "部门不存在");
        }

        List<String> idsToDelete = new ArrayList<>();
        idsToDelete.add(id);

        int index = 0;
        while (index < idsToDelete.size()) {
            String currentParentId = idsToDelete.get(index);
            List<VeDept4849> children = veDeptDaoService.selectByParentId(currentParentId);
            if (CollectionUtils.isNotEmpty(children)) {
                for (VeDept4849 child : children) {
                    idsToDelete.add(child.getId());
                }
            }
            index++;
        }

        Collections.reverse(idsToDelete);
        for (String deptId : idsToDelete) {
            veDeptDaoService.deleteById(deptId);
        }
    }

    // ==================== 改 ====================
    @Transactional(rollbackFor = Exception.class)
    public void update(VeDeptUpdDTO dto) throws SystemException {
        String id = dto.getId();
        VeDept4849 exist = veDeptDaoService.selectById(id);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "部门不存在");
        }
        validateParentExists(dto.getParentId());

        Date nowtime = new Date();
        VeDept4849 entity = new VeDept4849();
        entity.setId(id);
        entity.setDeptId(dto.getDeptId());
        entity.setQybh(dto.getQybh());
        entity.setBh(dto.getBh());
        entity.setShortName(dto.getShortName());
        entity.setParentId(dto.getParentId());
        entity.setDetailAddress(dto.getDetailAddress());
        entity.setStatus(dto.getStatus());
        entity.setDataSource(exist.getDataSource() != null ? exist.getDataSource() : "1");
        entity.setUpdateTime(nowtime);

        buildDeptPath(entity);

        veDeptDaoService.updateVeDept(entity);
    }

    // ==================== 查 ====================
    public List<VeDeptQryVO> queryList(Page<VeDeptQryVO> page, VeDeptQryDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            return new ArrayList<>();
        }
        return veDeptDaoService.queryList(page, dto);
    }

    public VeDept4849 queryOne(String id) {
        return veDeptDaoService.selectById(id);
    }

    // ==================== 多表 ====================
    public List<DeptTreeQryVO> queryDeptTree(DeptTreeQryDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            return new ArrayList<>();
        }
        return veDeptDaoService.queryDeptTree(dto);
    }

    // ==================== 私有方法 ====================

    private void validateParentExists(String parentId) throws SystemException {
        if (StringUtils.isNotBlank(parentId) && !"none".equals(parentId)) {
            VeDept4849 parent = veDeptDaoService.selectById(parentId);
            if (Objects.isNull(parent)) {
                throw new SystemException(DemoExceptionEnum.DEMO_0002, "上级部门不存在");
            }
        }
    }

    private void buildDeptPath(VeDept4849 entity) {
        String parentId = entity.getParentId();
        if ("none".equals(parentId) || StringUtils.isBlank(parentId)) {
            entity.setDeptIdPath(entity.getDeptId());
            entity.setDeptNamePath(entity.getShortName());
        } else {
            VeDept4849 parent = veDeptDaoService.selectById(parentId);
            if (parent != null && StringUtils.isNotBlank(parent.getDeptIdPath())) {
                entity.setDeptIdPath(parent.getDeptIdPath() + "," + entity.getDeptId());
                entity.setDeptNamePath(parent.getDeptNamePath() + "," + entity.getShortName());
            } else {
                entity.setDeptIdPath(entity.getDeptId());
                entity.setDeptNamePath(entity.getShortName());
            }
        }
    }
}