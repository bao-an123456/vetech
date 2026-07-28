package cn.vetech.charge.cloud.demo.server.mapper;

import cn.vetech.charge.cloud.database.base.BaseMapper;
import cn.vetech.charge.cloud.demo.server.entity.VeDept4849;
import cn.vetech.charge.cloud.demo.server.service.dto.dept.VeDeptQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.multi.DeptTreeQryVO;
import cn.vetech.charge.cloud.demo.server.service.vo.dept.VeDeptQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VeDeptMapper extends BaseMapper<VeDept4849> {

    /**
     * 分页查询部门列表
     */
    List<VeDeptQryVO> queryList(Page<VeDeptQryVO> page, @Param("dto") VeDeptQryDTO dto);

    /**
     * 根据企业编号和部门id查询
     */
    VeDept4849 selectByQybhAndDeptId(@Param("qybh") String qybh, @Param("deptId") String deptId);

    /**
     * 根据企业编号和部门编号查询
     */
    VeDept4849 selectByQybhAndBh(@Param("qybh") String qybh, @Param("bh") String bh);

    /**
     * 更新部门信息（支持全字段更新）
     */
    void updateVeDept(VeDept4849 entity);

    /**
     * 批量新增部门信息
     */
    void insertBatch(@Param("list") List<VeDept4849> list);

    /**
     * 批量更新部门信息
     */
    void updateBatch(@Param("list") List<VeDept4849> list);

    /**
     * 根据上级部门ID查询所有直接子部门
     */
    List<VeDept4849> selectByParentId(@Param("parentId") String parentId);

    /**
     * 部门树形结构查询
     */
    List<DeptTreeQryVO> selectDeptTree(@Param("qybh") String qybh);
}