package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.server.dao.VeDeptDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.DeptTreeQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptSaveDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptUpdDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.DeptTreeQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.dept.VeDeptQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class VeDeptBusinessService {

    @Autowired
    private VeDeptDaoServiceImpl veDeptDaoService;

    // ==================== 增 ====================
    @Transactional(rollbackFor = Exception.class)
    public void save(VeDeptSaveDTO dto) throws SystemException {
        // 校验 deptId 是否已存在
        VeDept4849 exist = veDeptDaoService.selectByQybhAndDeptId(dto.getQybh(), dto.getDeptId());
        if (exist != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该部门已存在，请勿重复添加");
        }
        // 校验 bh 是否已存在
        VeDept4849 exist2 = veDeptDaoService.selectByQybhAndBh(dto.getQybh(), dto.getBh());
        if (exist2 != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该部门编号已存在，请勿重复添加");
        }
        // 校验父部门是否存在（非根部门）
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
        entity.setCreateTime(nowtime);
        entity.setUpdateTime(nowtime);

        buildDeptPath(entity);

        veDeptDaoService.insertVeDept(entity);
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
        // 校验父部门是否存在（非根部门）
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