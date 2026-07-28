package cn.vetech.charge.cloud.demo.server.service;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.vetech.charge.cloud.demo.server.dao.VeDeptDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.dao.VeImportInsertDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.dao.VePositionDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.entity.VeImport4849;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeDeptMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VeEmpMapper;
import cn.vetech.charge.cloud.demo.server.mapper.VePositionMapper;
import cn.vetech.charge.cloud.demo.server.service.vo.importvo.VeDeptImportVO;
import cn.vetech.charge.cloud.demo.server.service.vo.importvo.VeEmpImportVO;
import cn.vetech.charge.fccapi.FccApiUserVO;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VeImportInsertService {

    private static final int BATCH_SIZE = 100;

    @Autowired
    private VeDeptMapper veDeptMapper;
    @Autowired
    private VeEmpMapper veEmpMapper;
    @Autowired
    private VeDeptDaoServiceImpl veDeptDaoService;
    @Autowired
    private VeImportInsertDaoServiceImpl veImportInsertDaoService;
    @Autowired
    private VePositionDaoServiceImpl vePositionDaoService;
    @Autowired
    private VePositionMapper vePositionMapper;

    private <T> List<T> parseExcel(String fileUrl, Class<T> clazz, ImportParams params) throws Exception {
        if (StringUtils.isBlank(fileUrl)) {
            throw new IllegalArgumentException("fileUrl不能为空");
        }
        if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
            try (InputStream inputStream = new URL(fileUrl).openStream()) {
                return ExcelImportUtil.importExcel(inputStream, clazz, params);
            }
        } else {
            return ExcelImportUtil.importExcel(new File(fileUrl), clazz, params);
        }
    }

    // ==================== 部门导入 ====================

    @Transactional(rollbackFor = Exception.class)
    public int importInsertDept(String fileUrl, FccApiUserVO openApiUserVO) {
        VeImport4849 task = createTask("1", "部门导入", openApiUserVO);

        int errorNum = 0;
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            List<VeDeptImportVO> importList = parseExcel(fileUrl, VeDeptImportVO.class, params);
            if (importList == null) {
                importList = new ArrayList<>();
            }

            List<VeDept4849> existList = veDeptMapper.selectList(new EntityWrapper<VeDept4849>().eq("qybh", openApiUserVO.getQybh()));
            Set<String> existDeptIds = existList != null ? existList.stream().map(VeDept4849::getDeptId).collect(Collectors.toSet()) : new HashSet<>();

            List<VeDept4849> successList = new ArrayList<>();
            Map<String, VeDept4849> bhMap = new HashMap<>();

            // 提取所有 Excel 中待导入的部门编号
            Set<String> excelBhSet = importList.stream().map(VeDeptImportVO::getBh).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());

            // 预校验非父子依赖基础项，过滤出候选列表
            List<VeDeptImportVO> candidateList = new ArrayList<>();
            for (VeDeptImportVO vo : importList) {
                String errorReason = validateDeptBasic(vo, openApiUserVO.getQybh(), bhMap, excelBhSet);
                if (StringUtils.isNotEmpty(errorReason)) {
                    errorNum++;
                } else {
                    candidateList.add(vo);
                }
            }

            // 多轮解析依赖（处理父部门在同一 Excel 中按任意顺序排列的情况）
            boolean progress = true;
            while (!candidateList.isEmpty() && progress) {
                progress = false;
                Iterator<VeDeptImportVO> iterator = candidateList.iterator();
                while (iterator.hasNext()) {
                    VeDeptImportVO vo = iterator.next();
                    if (canResolveParent(vo, openApiUserVO.getQybh(), bhMap)) {
                        VeDept4849 dept = buildDeptEntity(vo, openApiUserVO, bhMap, existDeptIds);
                        bhMap.put(vo.getBh(), dept);
                        successList.add(dept);
                        iterator.remove();
                        progress = true;
                    }
                }
            }

            // 剩余无法解析上级的 Candidate 计入错误
            errorNum += candidateList.size();

            for (VeDept4849 dept : successList) {
                veDeptMapper.insert(dept);
            }
            finishTask(task, "3", successList.size());
        } catch (Exception e) {
            finishTask(task, "4", 0);
            throw new RuntimeException("部门导入失败：" + e.getMessage(), e);
        }
        return errorNum;
    }

    private String validateDeptBasic(VeDeptImportVO vo, String qybh, Map<String, VeDept4849> bhMap, Set<String> excelBhSet) {
        if (vo == null) {
            return "数据为空";
        }
        if (StringUtils.isEmpty(vo.getBh())) {
            return "部门编号不能为空";
        }
        if (StringUtils.isEmpty(vo.getShortName())) {
            return "部门名称不能为空";
        }
        if (veDeptMapper.selectByQybhAndBh(qybh, vo.getBh()) != null) {
            return "部门编号已存在：" + vo.getBh();
        }
        if (bhMap.containsKey(vo.getBh())) {
            return "Excel内部门编号重复：" + vo.getBh();
        }
        if (StringUtils.isNotEmpty(vo.getParentBh())) {
            if (!excelBhSet.contains(vo.getParentBh()) && veDeptMapper.selectByQybhAndBh(qybh, vo.getParentBh()) == null) {
                return "上级部门编号不存在：" + vo.getParentBh();
            }
        }
        bhMap.put(vo.getBh(), null); // 标记已被本批次使用，防止同批次后续重名
        return null;
    }

    private boolean canResolveParent(VeDeptImportVO vo, String qybh, Map<String, VeDept4849> bhMap) {
        if (StringUtils.isEmpty(vo.getParentBh())) {
            return true; // 顶级部门直接可解析
        }
        if (bhMap.get(vo.getParentBh()) != null) {
            return true; // 本批次前面解析好的父部门
        }
        return veDeptMapper.selectByQybhAndBh(qybh, vo.getParentBh()) != null; // 数据库中存在的父部门
    }

    private VeDept4849 buildDeptEntity(VeDeptImportVO vo, FccApiUserVO user, Map<String, VeDept4849> bhMap, Set<String> existDeptIds) {
        VeDept4849 dept = new VeDept4849();
        dept.setId(IdGenerator.getHexId());
        dept.setQybh(user.getQybh());
        dept.setBh(vo.getBh());
        dept.setShortName(vo.getShortName());
        dept.setDetailAddress(vo.getDetailAddress());
        dept.setStatus("1");
        dept.setCreatorId(user.getYgid());
        dept.setDataSource("1");
        Date now = new Date();
        dept.setCreateTime(now);
        dept.setUpdateTime(now);

        VeDept4849 parent = null;
        if (StringUtils.isEmpty(vo.getParentBh())) {
            dept.setParentId("none");
            dept.setDeptId(generateTopDeptId(existDeptIds));
        } else {
            parent = bhMap.get(vo.getParentBh());
            if (parent == null) {
                parent = veDeptMapper.selectByQybhAndBh(user.getQybh(), vo.getParentBh());
            }
            if (parent == null) {
                throw new RuntimeException("找不到上级部门：" + vo.getParentBh());
            }
            dept.setParentId(parent.getId());
            dept.setDeptId(generateSubDeptId(parent, existDeptIds));
        }

        if (parent == null) {
            dept.setDeptIdPath(dept.getDeptId());
            dept.setDeptNamePath(dept.getShortName());
        } else {
            dept.setDeptIdPath(parent.getDeptIdPath() + "," + dept.getDeptId());
            dept.setDeptNamePath(parent.getDeptNamePath() + "," + dept.getShortName());
        }
        return dept;
    }

    private String generateTopDeptId(Set<String> existDeptIds) {
        int i = 1000;
        while (existDeptIds.contains(String.valueOf(i))) {
            i++;
        }
        String deptId = String.valueOf(i);
        existDeptIds.add(deptId);
        return deptId;
    }

    private String generateSubDeptId(VeDept4849 parent, Set<String> existDeptIds) {
        int i = 1000;
        String deptId;
        do {
            deptId = parent.getDeptId() + i;
            i++;
        } while (existDeptIds.contains(deptId));
        existDeptIds.add(deptId);
        return deptId;
    }

    // ==================== 员工导入 ====================

    @Transactional(rollbackFor = Exception.class)
    public int importInsertEmp(String fileUrl, FccApiUserVO openApiUserVO) {
        VeImport4849 task = createTask("1", "员工导入", openApiUserVO);
        int errorNum = 0;
        try {
            ImportParams params = new ImportParams();
            params.setHeadRows(1);
            List<VeEmpImportVO> importList = parseExcel(fileUrl, VeEmpImportVO.class, params);
            if (importList == null) {
                importList = new ArrayList<>();
            }
            String qybh = openApiUserVO.getQybh();

            List<VeEmp4849> existEmpList = veEmpMapper.selectList(
                    new EntityWrapper<VeEmp4849>().eq("qybh", qybh));
            Set<String> existGhSet = existEmpList != null ? existEmpList.stream().map(VeEmp4849::getGh).collect(Collectors.toSet()) : new HashSet<>();

            List<VeDept4849> deptList = veDeptMapper.selectList(
                    new EntityWrapper<VeDept4849>().eq("qybh", qybh));
            Map<String, String> bhToDeptIdMap = deptList != null ? deptList.stream().collect(Collectors.toMap(VeDept4849::getBh, VeDept4849::getDeptId, (a, b) -> a)) : new HashMap<>();

            Map<String, String> ghToIdMap = new HashMap<>();
            Set<String> batchGhSet = new HashSet<>();
            Set<String> allBatchGhSet = importList.stream().map(VeEmpImportVO::getGh).filter(StringUtils::isNotEmpty).collect(Collectors.toSet());

            List<VeEmpImportVO> validVoList = new ArrayList<>();
            List<VeEmp4849> empList = new ArrayList<>();

            for (VeEmpImportVO vo : importList) {
                String errorReason = validateEmp(vo, existGhSet, batchGhSet, bhToDeptIdMap, ghToIdMap, allBatchGhSet, qybh);
                if (StringUtils.isNotEmpty(errorReason)) {
                    errorNum++;
                    continue;
                }
                VeEmp4849 emp = buildEmpEntity(vo, openApiUserVO);
                ghToIdMap.put(vo.getGh(), emp.getId());
                batchGhSet.add(vo.getGh());
                validVoList.add(vo);
                empList.add(emp);
            }

            List<VePosition4849> positionList = new ArrayList<>();
            for (int i = 0; i < validVoList.size(); i++) {
                VeEmpImportVO vo = validVoList.get(i);
                VeEmp4849 emp = empList.get(i);
                VePosition4849 position = buildPositionEntity(vo, emp, openApiUserVO, bhToDeptIdMap, ghToIdMap);
                positionList.add(position);
            }

            for (int i = 0; i < empList.size(); i += BATCH_SIZE) {
                List<VeEmp4849> batch = empList.subList(i, Math.min(i + BATCH_SIZE, empList.size()));
                veEmpMapper.insertBatch(batch);
            }
            for (int i = 0; i < positionList.size(); i += BATCH_SIZE) {
                List<VePosition4849> batch = positionList.subList(i, Math.min(i + BATCH_SIZE, positionList.size()));
                vePositionMapper.insertBatch(batch);
            }

            finishTask(task, "3", empList.size());
        } catch (Exception e) {
            finishTask(task, "4", 0);
            throw new RuntimeException("员工导入失败：" + e.getMessage(), e);
        }
        return errorNum;
    }

    private String validateEmp(VeEmpImportVO vo, Set<String> existGhSet, Set<String> batchGhSet,
                                Map<String, String> bhToDeptIdMap, Map<String, String> ghToIdMap,
                                Set<String> allBatchGhSet, String qybh) {
        if (vo == null) return "数据为空";
        if (StringUtils.isEmpty(vo.getGh())) return "工号不能为空";
        if (StringUtils.isEmpty(vo.getName())) return "姓名不能为空";
        if (StringUtils.isEmpty(vo.getGender())) return "性别不能为空";
        if (existGhSet.contains(vo.getGh())) return "工号已存在：" + vo.getGh();
        if (batchGhSet.contains(vo.getGh())) return "Excel内工号重复：" + vo.getGh();
        if (StringUtils.isNotEmpty(vo.getDeptBh()) && !bhToDeptIdMap.containsKey(vo.getDeptBh())) return "部门编号不存在：" + vo.getDeptBh();

        if (StringUtils.isNotEmpty(vo.getSupervisorGh())) {
            if (!ghToIdMap.containsKey(vo.getSupervisorGh()) && !allBatchGhSet.contains(vo.getSupervisorGh())) {
                VeEmp4849 supervisorInDb = veEmpMapper.selectByQybhAndGh(qybh, vo.getSupervisorGh());
                if (supervisorInDb == null) {
                    return "直属上级工号不存在：" + vo.getSupervisorGh();
                }
            }
        }
        return null;
    }

    private VeEmp4849 buildEmpEntity(VeEmpImportVO vo, FccApiUserVO user) {
        VeEmp4849 emp = new VeEmp4849();
        emp.setId(IdGenerator.getHexId());
        emp.setQybh(user.getQybh());
        emp.setGh(vo.getGh());
        emp.setName(vo.getName());
        emp.setEnglishName(vo.getEnglishName());
        emp.setPhone(vo.getPhone());
        emp.setEmail(vo.getEmail());
        emp.setGender("男".equals(vo.getGender()) ? "M" : "F");
        emp.setBirthday(vo.getBirthday());
        emp.setAccountStatus("1");
        emp.setVersionNo(1);
        emp.setCreatorId(user.getYgid());
        emp.setDataSource("1");
        Date now = new Date();
        emp.setCreateTime(now);
        emp.setUpdateTime(now);
        return emp;
    }

    private VePosition4849 buildPositionEntity(VeEmpImportVO vo, VeEmp4849 emp, FccApiUserVO user,
                                               Map<String, String> bhToDeptIdMap, Map<String, String> ghToIdMap) {
        VePosition4849 position = new VePosition4849();
        position.setId(IdGenerator.getHexId());
        position.setQybh(user.getQybh());
        position.setYgid(emp.getId());
        if (StringUtils.isNotEmpty(vo.getDeptBh())) {
            position.setDeptId(bhToDeptIdMap.get(vo.getDeptBh()));
        }
        position.setJobLevel(parseJobLevel(vo.getJobLevel()));
        position.setPositionCode(vo.getPositionCode());
        position.setPositionName(vo.getPositionName());
        position.setHireDate(new Date());
        position.setStatus("1");
        position.setCreateTime(new Date());
        position.setUpdateTime(new Date());
        position.setCreatorId(user.getYgid());

        if (StringUtils.isNotEmpty(vo.getSupervisorGh())) {
            String supervisorId = ghToIdMap.get(vo.getSupervisorGh());
            if (supervisorId == null) {
                VeEmp4849 supervisorInDb = veEmpMapper.selectByQybhAndGh(user.getQybh(), vo.getSupervisorGh());
                if (supervisorInDb != null) {
                    supervisorId = supervisorInDb.getId();
                }
            }
            if (supervisorId != null) {
                position.setSupervisorId(supervisorId);
            }
        }
        return position;
    }

    private String parseJobLevel(String jobLevelText) {
        if (StringUtils.isEmpty(jobLevelText)) {
            return null;
        }
        String s = jobLevelText.replace("级", "").trim().toUpperCase();
        if (s.length() == 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'F') {
            return String.valueOf(s.charAt(0) - 'A' + 1);
        }
        return s;
    }

    // ==================== 私有方法 ====================

    private VeImport4849 createTask(String taskType, String taskName, FccApiUserVO user) {
        VeImport4849 task = new VeImport4849();
        task.setId(IdGenerator.getHexId());
        task.setQybh(user.getQybh());
        task.setTaskType(taskType);
        task.setStatus("1");
        task.setTaskName(taskName);
        task.setStartTime(new Date());
        task.setCreatorId(user.getYgid());
        task.setCreateTime(new Date());
        veImportInsertDaoService.save(task);
        return task;
    }

    private void finishTask(VeImport4849 task, String status, int count) {
        task.setStatus(status);
        task.setRecordCount(count);
        Date endTime = new Date();
        task.setEndTime(endTime);
        task.setCostTime((int) (endTime.getTime() - task.getStartTime().getTime()));
        veImportInsertDaoService.update(task);
    }
}