package cn.vetech.charge.cloud.demo.server.dao;

import cn.vetech.charge.cloud.database.base.BaseServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeDeptMapper;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.DeptTreeQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.DeptTreeQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.dept.VeDeptQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class VeDeptDaoServiceImpl extends BaseServiceImpl<VeDeptMapper, VeDept4849> {

    /*
    新增时查询企业编号和部门ID来判断是否存在
     */
    public VeDept4849 selectByQybhAndDeptId(String qybh, String deptId) {
        return baseMapper.selectByQybhAndDeptId(qybh, deptId);
    }

    /*
    新增时查询编号是否存在
     */
    public VeDept4849 selectByQybhAndBh(String qybh, String bh) {
        return baseMapper.selectByQybhAndBh(qybh, bh);
    }

    public void insertVeDept(VeDept4849 entity) {
        baseMapper.insert(entity);
    }

    public void deleteById(String id) {
        baseMapper.deleteById(id);
    }

    public void updateVeDept(VeDept4849 entity) {
        baseMapper.updateVeDept(entity);
    }

    public VeDept4849 selectById(String id) {
        return baseMapper.selectById(id);
    }

    public List<VeDeptQryVO> queryList(Page<VeDeptQryVO> page, VeDeptQryDTO dto) {
        return baseMapper.queryList(page, dto);
    }

    public List<VeDept4849> selectByParentId(String parentId) {
        return baseMapper.selectByParentId(parentId);
    }

    // ==================== 多表 ====================
    public List<DeptTreeQryVO> queryDeptTree(DeptTreeQryDTO dto) {
        List<DeptTreeQryVO> allDepts = baseMapper.selectDeptTree(dto.getQybh());
        if (CollectionUtils.isEmpty(allDepts)) {
            return new ArrayList<>();
        }
        String parentId = StringUtils.hasText(dto.getParentId()) ? dto.getParentId() : "none";
        return buildTree(allDepts, parentId);
    }

    private List<DeptTreeQryVO> buildTree(List<DeptTreeQryVO> allDepts, String parentId) {
        List<DeptTreeQryVO> tree = new ArrayList<>();
        for (DeptTreeQryVO dept : allDepts) {
            boolean isMatch = "none".equals(parentId)
                    ? ("none".equals(dept.getParentId()) || !StringUtils.hasText(dept.getParentId()))
                    : parentId.equals(dept.getParentId());
            if (isMatch) {
                List<DeptTreeQryVO> children = buildTree(allDepts, dept.getId());
                dept.setChildren(children);
                dept.setLeaf(CollectionUtils.isEmpty(children));
                tree.add(dept);
            }
        }
        return tree;
    }
}