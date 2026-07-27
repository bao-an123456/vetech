package cn.vetech.charge.cloud.demo.server.mapper;

import cn.vetech.charge.cloud.database.base.BaseMapper;
import cn.vetech.charge.cloud.demo.server.entity.VePosition4849;
import cn.vetech.charge.cloud.demo.server.service.dto.position.VePositionQryDTO;
import cn.vetech.charge.cloud.demo.server.service.vo.position.VePositionQryVO;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VePositionMapper extends BaseMapper<VePosition4849> {

    /**
     * 批量新增
     */
    void insertBatch(@Param("list") List<VePosition4849> list);

    /**
     * 更新任职信息
     */
    void updateVePosition(VePosition4849 entity);

    /**
     * 分页查询
     */
    List<VePositionQryVO> queryList(Page<VePositionQryVO> page, @Param("dto") VePositionQryDTO dto);

    /**
     * 根据企业编号+员工ID查重
     */
    VePosition4849 selectByQybhAndYgid(@Param("qybh") String qybh, @Param("ygid") String ygid);
}