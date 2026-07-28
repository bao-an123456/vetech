package cn.vetech.charge.cloud.demo.server.mapper;

import cn.vetech.charge.cloud.database.base.BaseMapper;
import cn.vetech.charge.cloud.demo.server.entity.VeEmp4849;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpByDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.multi.EmpWithDeptPosQryDTO;
import cn.vetech.charge.cloud.demo.server.service.dto.emp.VeEmpQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpByDeptQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.EmpWithDeptPosVO;
import cn.vetech.charge.cloud.demo.server.service.vo.emp.VeEmpQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VeEmpMapper extends BaseMapper<VeEmp4849> {

    /**
     * 批量新增
     */
    void insertBatch(@Param("list") List<VeEmp4849> list);

    /**
     * 批量更新
     */
    void updateBatch(@Param("list") List<VeEmp4849> list);

    /**
     * 更新员工信息
     */
    void updateVeEmp(VeEmp4849 entity);

    /**
     * 分页查询员工列表
     */
    List<VeEmpQryVO> queryList(Page<VeEmpQryVO> page, @Param("dto") VeEmpQryDTO dto);

    /**
     * 根据企业编号和工号查询员工（用于新增/更新时工号唯一性校验）
     */
    VeEmp4849 selectByQybhAndGh(@Param("qybh") String qybh, @Param("gh") String gh);

    // ==================== 多表 ====================
    /**
     * 员工列表带部门/岗位信息（三表关联分页）
     */
    List<EmpWithDeptPosVO> selectEmpListWithDeptAndPos(Page<EmpWithDeptPosVO> page, @Param("dto") EmpWithDeptPosQryDTO dto);

    /**
     * 查询某部门下的所有员工（分页）
     */
    List<EmpByDeptQryVO> selectEmpListByDept(Page<EmpByDeptQryVO> page, @Param("dto") EmpByDeptQryDTO dto);
}