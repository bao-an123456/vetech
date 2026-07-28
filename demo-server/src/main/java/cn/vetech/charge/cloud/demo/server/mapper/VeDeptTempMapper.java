package cn.vetech.charge.cloud.demo.server.mapper;

import cn.vetech.charge.cloud.database.base.BaseMapper;
import cn.vetech.charge.cloud.demo.server.entity.VeDeptTemp4849;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface VeDeptTempMapper extends BaseMapper<VeDeptTemp4849> {

    void insertBatch(@Param("list") List<VeDeptTemp4849> list);

    Date selectMaxUpdateTime(@Param("qybh") String qybh);

    void clearTempTable(@Param("qybh") String qybh);
}
