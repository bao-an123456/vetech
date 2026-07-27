package cn.vetech.charge.cloud.demo.server.dao;

import cn.vetech.charge.cloud.database.base.BaseServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.mapper.VeEmpMapper;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpByDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpWithDeptPosQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpByDeptQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpWithDeptPosVO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VeEmpDaoServiceImpl extends BaseServiceImpl<VeEmpMapper, VeEmp4849> {

    public void insertVeEmp(VeEmp4849 entity) {
        baseMapper.insert(entity);
    }

    public void insertEmpBatch(List<VeEmp4849> list) {
        baseMapper.insertBatch(list);
    }

    public void deleteById(String id) {
        baseMapper.deleteById(id);
    }

    public void updateVeEmp(VeEmp4849 entity) {
        baseMapper.updateVeEmp(entity);
    }

    public void updateEmpById(VeEmp4849 entity) {
        baseMapper.updateById(entity);
    }

    public VeEmp4849 selectById(String id) {
        return baseMapper.selectById(id);
    }

    public VeEmp4849 selectByQybhAndGh(String qybh, String gh) {
        return baseMapper.selectByQybhAndGh(qybh, gh);
    }

    public List<VeEmpQryVO> queryList(Page<VeEmpQryVO> page, VeEmpQryDTO dto) {
        return baseMapper.queryList(page, dto);
    }

    // ==================== 多表 ====================
    public List<EmpWithDeptPosVO> queryEmpListWithDeptAndPos(Page<EmpWithDeptPosVO> page, EmpWithDeptPosQryDTO dto) {
        return baseMapper.selectEmpListWithDeptAndPos(page, dto);
    }

    public List<EmpByDeptQryVO> queryEmpListByDept(Page<EmpByDeptQryVO> page, EmpByDeptQryDTO dto) {
        return baseMapper.selectEmpListByDept(page, dto);
    }
}