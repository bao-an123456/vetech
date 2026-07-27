package cn.vetech.charge.cloud.demo.server.service;

import cn.vetech.charge.cloud.demo.common.constant.DemoExceptionEnum;
import cn.vetech.charge.cloud.demo.server.dao.VePositionDaoServiceImpl;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionSaveDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionUpdDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.position.VePositionQryVO;
import cn.vetech.charge.cloud.exception.SystemException;
import cn.vetech.charge.cloud.modules.utils.IdGenerator;
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
public class VePositionBusinessService {

    @Autowired
    private VePositionDaoServiceImpl vePositionDaoService;

    // ==================== 增 ====================

    @Transactional(rollbackFor = Exception.class)
    public void save(VePositionSaveDTO dto) throws SystemException {
        // 校验同一企业下同一员工是否已有任职
        VePosition4849 exist = vePositionDaoService.selectByQybhAndYgid(dto.getQybh(), dto.getYgid());
        if (exist != null) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "该员工已存在任职信息，请勿重复添加");
        }

        Date nowtime = new Date();
        String id = IdGenerator.getHexId();

        VePosition4849 entity = new VePosition4849();
        entity.setId(id);
        entity.setQybh(dto.getQybh());
        entity.setDeptId(dto.getDeptId());
        entity.setYgid(dto.getYgid());
        entity.setJobLevel(dto.getJobLevel());
        entity.setSupervisorId(dto.getSupervisorId());
        entity.setHireDate(dto.getHireDate());
        entity.setPositionCode(dto.getPositionCode());
        entity.setPositionName(dto.getPositionName());
        entity.setStatus(dto.getStatus());
        entity.setCreatorId("me");
        entity.setCreateTime(nowtime);
        entity.setUpdateTime(nowtime);

        vePositionDaoService.insertPosition(entity);
    }

    // ==================== 删 ====================

    public void delete(String id) throws SystemException {
        VePosition4849 exist = vePositionDaoService.selectById(id);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "任职信息不存在");
        }
        vePositionDaoService.deleteById(id);
    }

    // ==================== 改 ====================

    @Transactional(rollbackFor = Exception.class)
    public void update(VePositionUpdDTO dto) throws SystemException {
        String id = dto.getId();
        VePosition4849 exist = vePositionDaoService.selectById(id);
        if (Objects.isNull(exist)) {
            throw new SystemException(DemoExceptionEnum.DEMO_0002, "任职信息不存在");
        }

        Date nowtime = new Date();
        VePosition4849 entity = new VePosition4849();
        entity.setId(id);
        entity.setQybh(dto.getQybh());
        entity.setDeptId(dto.getDeptId());
        entity.setYgid(dto.getYgid());
        entity.setJobLevel(dto.getJobLevel());
        entity.setSupervisorId(dto.getSupervisorId());
        entity.setHireDate(dto.getHireDate());
        entity.setPositionCode(dto.getPositionCode());
        entity.setPositionName(dto.getPositionName());
        entity.setStatus(dto.getStatus());
        entity.setUpdateTime(nowtime);

        vePositionDaoService.updateVePosition(entity);
    }

    // ==================== 查 ====================

    public List<VePositionQryVO> queryList(Page<VePositionQryVO> page, VePositionQryDTO dto) {
        if (Objects.isNull(dto) || StringUtils.isBlank(dto.getQybh())) {
            return new ArrayList<>();
        }
        return vePositionDaoService.queryList(page, dto);
    }

    public VePosition4849 queryOne(String id) {
        return vePositionDaoService.selectById(id);
    }
}