package cn.vetech.charge.cloud.demo.server.service.dto.position;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "任职信息查询")
public class VePositionQryDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String deptId;

    @ApiModelProperty(value = "员工ID号", dataType = "string", notes = "员工ID号")
    private String ygid;

    @ApiModelProperty(value = "职级", dataType = "string", notes = "职级")
    private String jobLevel;

    @ApiModelProperty(value = "直接上级主管", dataType = "string", notes = "直接上级主管")
    private String supervisorId;

    @ApiModelProperty(value = "岗位编号", dataType = "string", notes = "岗位编号")
    private String positionCode;

    @ApiModelProperty(value = "岗位名称", dataType = "string", notes = "岗位名称")
    private String positionName;

    @ApiModelProperty(value = "状态（0停用 1启用）", dataType = "string", notes = "状态")
    private String status;

    @ApiModelProperty(value = "分页current", dataType = "int", notes = "当前页")
    private int current;

    @ApiModelProperty(value = "分页size", dataType = "int", notes = "分页size")
    private int size;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}