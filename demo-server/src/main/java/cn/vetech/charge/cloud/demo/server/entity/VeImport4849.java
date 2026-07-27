package cn.vetech.charge.cloud.demo.server.entity;

import cn.vetech.charge.cloud.database.base.BaseEntity;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_drdc_rw_4849")
public class VeImport4849 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "任务类型，1导入，2导出", dataType = "string")
    private String taskType;

    @ApiModelProperty(value = "状态，0为未执行,1正在执行，2终止，3完成，4失败", dataType = "string")
    private String status;

    @ApiModelProperty(value = "任务名称，如：员工导入 员工导出", dataType = "string")
    private String taskName;

    @ApiModelProperty(value = "导入导出的记录数", dataType = "int")
    private Integer recordCount;

    @ApiModelProperty(value = "开始时间", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "执行耗时，毫秒", dataType = "int")
    private Integer costTime;

    @ApiModelProperty(value = "创建人，ID来源员工表", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "创建时间，2020-01-01 12:12:12", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}