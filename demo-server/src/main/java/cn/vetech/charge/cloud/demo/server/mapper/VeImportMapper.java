package cn.vetech.charge.cloud.demo.server.mapper;

import cn.vetech.charge.cloud.database.base.BaseMapper;
import cn.vetech.charge.cloud.demo.server.entity.VeImport4849;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 导入任务表 Mapper 接口
 */
@Mapper
public interface VeImportMapper extends BaseMapper<VeImport4849> {

    /**
     * 根据 ID 查询任务
     */
    VeImport4849 selectByTaskId(@Param("id") String id);

    /**
     * 查询任务列表（按创建时间倒序）
     */
    List<VeImport4849> queryList(@Param("qybh") String qybh, @Param("taskType") String taskType);

    /**
     * 更新任务执行结果（状态、记录数、结束时间、耗时）
     */
    void updateVeImportExport(VeImport4849 entity);
}