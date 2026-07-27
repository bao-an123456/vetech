package cn.vetech.charge.cloud.demo.server.service.vo.position;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "任职信息详情")
public class VePositionQryDetailVO {

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "员工ID号", dataType = "string")
    private String ygid;

    @ApiModelProperty(value = "职级", dataType = "string")
    private String jobLevel;

    @ApiModelProperty(value = "直接上级主管", dataType = "string")
    private String supervisorId;

    @ApiModelProperty(value = "入职时间", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date hireDate;

    @ApiModelProperty(value = "岗位编号", dataType = "string")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称", dataType = "string")
    private String positionName;

    @ApiModelProperty(value = "状态", dataType = "string")
    private String status;

    @ApiModelProperty(value = "创建人", dataType = "string")
    private String creatorId;

    @ApiModelProperty(value = "创建时间", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间", dataType = "date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}