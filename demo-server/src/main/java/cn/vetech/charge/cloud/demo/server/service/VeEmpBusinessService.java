package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.server.dao.VeDeptDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.dao.VeEmpDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpByDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpWithDeptPosQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.*;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpByDeptQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpWithDeptPosVO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryDetailVO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
import cn.vetech.charge.cloud.modules.utils.mapper.BeanMapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class VeEmpBusinessService {

    @Autowired
    private VeEmpDaoServiceImpl veEmpDaoService;

    @Autowired
    private VeDeptDaoServiceImpl veDeptDaoService;

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
        entity.setUpdateTime(nowtime);

        veEmpDaoService.updateVeEmp(entity);
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