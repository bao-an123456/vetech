package cn.vetech.charge.cloud.demo.server.service.dto.dept;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "部门查询")
public class VeDeptQryDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门编号", dataType = "string", notes = "部门编号")
    private String bh;

    @ApiModelProperty(value = "简称", dataType = "string", notes = "简称")
    private String shortName;

    @ApiModelProperty(value = "上级部门ID", dataType = "string", notes = "上级部门ID")
    private String parentId;

    @ApiModelProperty(value = "状态（0停用 1启用）", dataType = "string", notes = "状态（0停用 1启用）")
    private String status;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}