package cn.vetech.charge.cloud.demo.server.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.vetech.charge.cloud.demo.fccapi.importsave.VeImportProgressResponse;
import cn.vetech.charge.cloud.demo.server.dao.VeImportInsertDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.entity.VeImport4849;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeDeptMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VeEmpMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VePositionMapper;
import cn.vetech.charge.cloud.demo.server.service.vo.importvo.VeDeptImportVO;
import cn.vetech.charge.cloud.demo.server.service.vo.importvo.VeEmpImportVO;
import cn.vetech.charge.cloud.demo.common.config.SyncConfigProperties;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 覆盖保存导入服务 (ImportSave: 存量更新/增量新增)
 * 采用内存/临时表索引预热 + 分批落库 + clear防OOM + 异步解耦进度反馈
 */
@Service
public class VeImportSaveService {

    private static final Logger log = LoggerFactory.getLogger(VeImportSaveService.class);

    @Autowired
    private VeDeptMapper veDeptMapper;
    @Autowired
    private VeEmpMapper veEmpMapper;
    @Autowired
    private VePositionMapper vePositionMapper;
    @Autowired
    private VeImportInsertDaoServiceImpl veImportInsertDaoService;
    @Autowired
    private SyncConfigProperties syncConfigProperties;

    private static <T> List<List<T>> partitionList(Collection<T> collection, int size) {
        List<List<T>> result = new ArrayList<>();
        List<T> current = new ArrayList<>(size);
        for (T item : collection) {
            current.add(item);
            if (current.size() == size) {
                result.add(current);
                current = new ArrayList<>(size);
            }
        }
        if (!current.isEmpty()) {
            result.add(current);
        }
        return result;
    }

    private <T> List<T> parseExcel(String fileUrl, Class<T> clazz, ImportParams params) throws Exception {
        if (StringUtils.isBlank(fileUrl)) {
            throw new IllegalArgumentException("fileUrl不能为空");
        }
        String cleanUrl = fileUrl.trim();
        if (cleanUrl.startsWith("file:///")) {
            cleanUrl = cleanUrl.substring(8);
        } else if (cleanUrl.startsWith("file:/")) {
            cleanUrl = cleanUrl.substring(6);
        }

        if (cleanUrl.startsWith("http://") || cleanUrl.startsWith("https://")) {
            try (InputStream inputStream = new URL(cleanUrl).openStream()) {
                return ExcelImportUtil.importExcel(inputStream, clazz, params);
            }
        } else {
            return ExcelImportUtil.importExcel(new File(cleanUrl), clazz, params);
        }
    }

    // ==================== 员工覆盖保存 (ImportSave) 异步入口 ====================

    public String importSaveEmpAsync(String fileUrl, FccApiUserVO openApiUserVO) {
        String taskId = IdGenerator.getHexId();
        VeImport4849 task = new VeImport4849();
        task.setId(taskId);
        task.setQybh(openApiUserVO.getQybh());
        task.setTaskType("1");
        task.setTaskName("员工覆盖保存(ImportSave)");
        task.setStatus("1"); // 正在执行
        task.setStartTime(new Date());
        task.setCreatorId(openApiUserVO.getYgid());
        task.setRecordCount(0);
        veImportInsertDaoService.save(task);

        // 异步后台线程慢慢跑阶段 1~3
        CompletableFuture.runAsync(() -> {
            int processedCount = 0;
            try {
                ImportParams params = new ImportParams();
                params.setHeadRows(1);
                List<VeEmpImportVO> importList = parseExcel(fileUrl, VeEmpImportVO.class, params);
                if (importList == null) {
                    importList = new ArrayList<>();
                }
                String qybh = openApiUserVO.getQybh();
                int batchSize = syncConfigProperties.getPageSize();
                task.setRecordCount(importList.size());
                veImportInsertDaoService.update(task);

                // 阶段 1: 内存/数据库索引预热 (按 500 个一批切片检索 ID + QYBH + GH)
                Set<String> allBatchGhSet = importList.stream().map(VeEmpImportVO::getGh).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
                Set<String> allBatchDeptBhSet = importList.stream().map(VeEmpImportVO::getDeptBh).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());

                Map<String, VeEmp4849> existEmpMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(allBatchGhSet)) {
                    for (List<String> ghChunk : partitionList(allBatchGhSet, batchSize)) {
                        List<VeEmp4849> dbEmps = veEmpMapper.selectList(new EntityWrapper<VeEmp4849>().eq("qybh", qybh).in("gh", ghChunk));
                        if (dbEmps != null) {
                            for (VeEmp4849 e : dbEmps) {
                                existEmpMap.put(e.getGh(), e);
                            }
                        }
                    }
                }

                Map<String, VePosition4849> existPosMap = new HashMap<>();
                if (!existEmpMap.isEmpty()) {
                    List<String> ygids = existEmpMap.values().stream().map(VeEmp4849::getId).collect(Collectors.toList());
                    for (List<String> ygidChunk : partitionList(ygids, batchSize)) {
                        List<VePosition4849> dbPositions = vePositionMapper.selectList(new EntityWrapper<VePosition4849>().eq("qybh", qybh).in("ygid", ygidChunk));
                        if (dbPositions != null) {
                            for (VePosition4849 p : dbPositions) {
                                existPosMap.put(p.getYgid(), p);
                            }
                        }
                    }
                }

                Map<String, String> bhToDeptIdMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(allBatchDeptBhSet)) {
                    for (List<String> deptBhChunk : partitionList(allBatchDeptBhSet, batchSize)) {
                        List<VeDept4849> dbDepts = veDeptMapper.selectList(new EntityWrapper<VeDept4849>().eq("qybh", qybh).in("bh", deptBhChunk));
                        if (dbDepts != null) {
                            for (VeDept4849 d : dbDepts) {
                                bhToDeptIdMap.put(d.getBh(), d.getDeptId());
                            }
                        }
                    }
                }

                // 阶段 2: 内存路由 (判断在黑板上还是新员工，分发到新增/更新队列)
                List<VeEmp4849> insertEmpList = new ArrayList<>();
                List<VeEmp4849> updateEmpList = new ArrayList<>();
                List<VePosition4849> insertPosList = new ArrayList<>();
                List<VePosition4849> updatePosList = new ArrayList<>();

                for (VeEmpImportVO vo : importList) {
                    if (vo == null || StringUtils.isEmpty(vo.getGh())) {
                        continue;
                    }
                    Date now = new Date();
                    boolean isExist = existEmpMap.containsKey(vo.getGh());

                    if (isExist) {
                        // 存量老员工 ➔ UPDATE
                        VeEmp4849 oldEmp = existEmpMap.get(vo.getGh());
                        VeEmp4849 emp = new VeEmp4849();
                        emp.setId(oldEmp.getId());
                        emp.setQybh(qybh);
                        emp.setGh(vo.getGh());
                        emp.setName(vo.getName());
                        emp.setEnglishName(vo.getEnglishName());
                        emp.setPhone(vo.getPhone());
                        emp.setEmail(vo.getEmail());
                        emp.setGender("男".equals(vo.getGender()) ? "M" : "F");
                        emp.setBirthday(vo.getBirthday());
                        emp.setAccountStatus("1");
                        emp.setVersionNo(oldEmp.getVersionNo() != null ? oldEmp.getVersionNo() + 1 : 1);
                        emp.setDataSource(oldEmp.getDataSource() != null ? oldEmp.getDataSource() : "1");
                        emp.setUpdateTime(now);
                        updateEmpList.add(emp);

                        VePosition4849 oldPos = existPosMap.get(oldEmp.getId());
                        VePosition4849 pos = new VePosition4849();
                        pos.setId(oldPos != null ? oldPos.getId() : IdGenerator.getHexId());
                        pos.setQybh(qybh);
                        pos.setYgid(oldEmp.getId());
                        if (StringUtils.isNotEmpty(vo.getDeptBh())) {
                            pos.setDeptId(bhToDeptIdMap.get(vo.getDeptBh()));
                        }
                        pos.setPositionCode(vo.getPositionCode());
                        pos.setPositionName(vo.getPositionName());
                        pos.setStatus("1");
                        pos.setUpdateTime(now);
                        updatePosList.add(pos);
                    } else {
                        // 新员工 ➔ INSERT
                        VeEmp4849 emp = new VeEmp4849();
                        emp.setId(IdGenerator.getHexId());
                        emp.setQybh(qybh);
                        emp.setGh(vo.getGh());
                        emp.setName(vo.getName());
                        emp.setEnglishName(vo.getEnglishName());
                        emp.setPhone(vo.getPhone());
                        emp.setEmail(vo.getEmail());
                        emp.setGender("男".equals(vo.getGender()) ? "M" : "F");
                        emp.setBirthday(vo.getBirthday());
                        emp.setAccountStatus("1");
                        emp.setVersionNo(1);
                        emp.setCreatorId(openApiUserVO.getYgid());
                        emp.setDataSource("1");
                        emp.setCreateTime(now);
                        emp.setUpdateTime(now);

                        existEmpMap.put(vo.getGh(), emp); // 记录到黑板，防止同一批重复
                        insertEmpList.add(emp);

                        VePosition4849 pos = new VePosition4849();
                        pos.setId(IdGenerator.getHexId());
                        pos.setQybh(qybh);
                        pos.setYgid(emp.getId());
                        if (StringUtils.isNotEmpty(vo.getDeptBh())) {
                            pos.setDeptId(bhToDeptIdMap.get(vo.getDeptBh()));
                        }
                        pos.setPositionCode(vo.getPositionCode());
                        pos.setPositionName(vo.getPositionName());
                        pos.setHireDate(now);
                        pos.setStatus("1");
                        pos.setCreatorId(openApiUserVO.getYgid());
                        pos.setCreateTime(now);
                        pos.setUpdateTime(now);
                        insertPosList.add(pos);
                    }
                    processedCount++;

                    // 阶段 3: 分批落库与防 OOM 清空 (满 batchSize 写入并 clear)
                    if (insertEmpList.size() >= batchSize) {
                        veEmpMapper.insertBatch(insertEmpList);
                        vePositionMapper.insertBatch(insertPosList);
                        insertEmpList.clear();
                        insertPosList.clear();
                        updateProgress(task, processedCount);
                    }
                    if (updateEmpList.size() >= batchSize) {
                        veEmpMapper.updateBatch(updateEmpList);
                        vePositionMapper.updateBatch(updatePosList);
                        updateEmpList.clear();
                        updatePosList.clear();
                        updateProgress(task, processedCount);
                    }
                }

                // 刷新尾数队列
                if (!insertEmpList.isEmpty()) {
                    veEmpMapper.insertBatch(insertEmpList);
                    vePositionMapper.insertBatch(insertPosList);
                    insertEmpList.clear();
                    insertPosList.clear();
                }
                if (!updateEmpList.isEmpty()) {
                    veEmpMapper.updateBatch(updateEmpList);
                    vePositionMapper.updateBatch(updatePosList);
                    updateEmpList.clear();
                    updatePosList.clear();
                }

                finishTask(task, "3", processedCount);
            } catch (Exception e) {
                log.error("员工 Save 导入异步执行失败 task=" + taskId, e);
                finishTask(task, "4", processedCount);
            }
        });

        return taskId;
    }

    // ==================== 部门覆盖保存 (ImportSave) 异步入口 ====================

    public String importSaveDeptAsync(String fileUrl, FccApiUserVO openApiUserVO) {
        String taskId = IdGenerator.getHexId();
        VeImport4849 task = new VeImport4849();
        task.setId(taskId);
        task.setQybh(openApiUserVO.getQybh());
        task.setTaskType("1");
        task.setTaskName("部门覆盖保存(ImportSave)");
        task.setStatus("1");
        task.setStartTime(new Date());
        task.setCreatorId(openApiUserVO.getYgid());
        task.setRecordCount(0);
        veImportInsertDaoService.save(task);

        CompletableFuture.runAsync(() -> {
            int processedCount = 0;
            try {
                ImportParams params = new ImportParams();
                params.setHeadRows(1);
                List<VeDeptImportVO> importList = parseExcel(fileUrl, VeDeptImportVO.class, params);
                if (importList == null) {
                    importList = new ArrayList<>();
                }
                String qybh = openApiUserVO.getQybh();
                int batchSize = syncConfigProperties.getPageSize();
                task.setRecordCount(importList.size());
                veImportInsertDaoService.update(task);

                // 检索已存在的部门 (按 500 个一批分块查询)
                Set<String> allBhs = importList.stream().map(VeDeptImportVO::getBh).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
                Map<String, VeDept4849> existDeptMap = new HashMap<>();
                if (CollectionUtils.isNotEmpty(allBhs)) {
                    for (List<String> bhChunk : partitionList(allBhs, batchSize)) {
                        List<VeDept4849> dbDepts = veDeptMapper.selectList(new EntityWrapper<VeDept4849>().eq("qybh", qybh).in("bh", bhChunk));
                        if (dbDepts != null) {
                            for (VeDept4849 d : dbDepts) {
                                existDeptMap.put(d.getBh(), d);
                            }
                        }
                    }
                }

                List<VeDept4849> insertList = new ArrayList<>();
                List<VeDept4849> updateList = new ArrayList<>();

                for (VeDeptImportVO vo : importList) {
                    if (vo == null || StringUtils.isEmpty(vo.getBh())) {
                        continue;
                    }
                    Date now = new Date();
                    boolean isExist = existDeptMap.containsKey(vo.getBh());

                    if (isExist) {
                        VeDept4849 old = existDeptMap.get(vo.getBh());
                        VeDept4849 dept = new VeDept4849();
                        dept.setId(old.getId());
                        dept.setDeptId(old.getDeptId());
                        dept.setQybh(qybh);
                        dept.setBh(vo.getBh());
                        dept.setShortName(vo.getShortName());
                        dept.setDetailAddress(vo.getDetailAddress());
                        dept.setStatus("1");
                        dept.setDataSource(old.getDataSource() != null ? old.getDataSource() : "1");
                        dept.setUpdateTime(now);
                        updateList.add(dept);
                    } else {
                        VeDept4849 dept = new VeDept4849();
                        dept.setId(IdGenerator.getHexId());
                        dept.setDeptId(IdGenerator.getHexId());
                        dept.setQybh(qybh);
                        dept.setBh(vo.getBh());
                        dept.setShortName(vo.getShortName());
                        dept.setDetailAddress(vo.getDetailAddress());
                        dept.setStatus("1");
                        dept.setCreatorId(openApiUserVO.getYgid());
                        dept.setDataSource("1");
                        dept.setCreateTime(now);
                        dept.setUpdateTime(now);
                        existDeptMap.put(vo.getBh(), dept);
                        insertList.add(dept);
                    }
                    processedCount++;

                    if (insertList.size() >= batchSize) {
                        veDeptMapper.insertBatch(insertList);
                        insertList.clear();
                        updateProgress(task, processedCount);
                    }
                    if (updateList.size() >= batchSize) {
                        veDeptMapper.updateBatch(updateList);
                        updateList.clear();
                        updateProgress(task, processedCount);
                    }
                }

                if (!insertList.isEmpty()) {
                    veDeptMapper.insertBatch(insertList);
                    insertList.clear();
                }
                if (!updateList.isEmpty()) {
                    veDeptMapper.updateBatch(updateList);
                    updateList.clear();
                }

                finishTask(task, "3", processedCount);
            } catch (Exception e) {
                log.error("部门 Save 导入异步执行失败 task=" + taskId, e);
                finishTask(task, "4", processedCount);
            }
        });

        return taskId;
    }

    // ==================== 进度查询服务 (给前端百分比进度条) ====================

    public VeImportProgressResponse getTaskProgress(String taskId) {
        VeImportProgressResponse response = new VeImportProgressResponse();
        response.setTaskId(taskId);
        if (StringUtils.isBlank(taskId)) {
            response.setStatus("0");
            response.setPercent(0);
            return response;
        }

        VeImport4849 task = veImportInsertDaoService.getById(taskId);
        if (task == null) {
            response.setStatus("0");
            response.setPercent(0);
            return response;
        }

        response.setTaskName(task.getTaskName());
        response.setStatus(task.getStatus());
        int total = task.getRecordCount() != null ? task.getRecordCount() : 0;
        response.setTotalCount(total);
        response.setCostTime(task.getCostTime() != null ? task.getCostTime() : 0);

        if ("3".equals(task.getStatus())) {
            // 已完成
            response.setProcessedCount(total);
            response.setPercent(100);
        } else if ("4".equals(task.getStatus())) {
            // 失败
            response.setProcessedCount(0);
            response.setPercent(0);
        } else {
            // 正在执行
            int processed = task.getRecordCount() != null ? task.getRecordCount() : 0;
            response.setProcessedCount(processed);
            if (total > 0) {
                int percent = (int) Math.min(99, Math.round(((double) processed / total) * 100));
                response.setPercent(percent);
            } else {
                response.setPercent(0);
            }
        }
        return response;
    }

    // ==================== 私有辅助方法 ====================

    private void updateProgress(VeImport4849 task, int processedCount) {
        task.setRecordCount(processedCount);
        veImportInsertDaoService.update(task);
    }

    private void finishTask(VeImport4849 task, String status, int processedCount) {
        task.setStatus(status);
        task.setRecordCount(processedCount);
        Date endTime = new Date();
        task.setEndTime(endTime);
        if (task.getStartTime() != null) {
            task.setCostTime((int) (endTime.getTime() - task.getStartTime().getTime()));
        }
        veImportInsertDaoService.update(task);
    }
}
