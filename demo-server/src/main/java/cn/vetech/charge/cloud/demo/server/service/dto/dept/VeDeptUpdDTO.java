package cn.vetech.charge.cloud.demo.server.service.dto.dept;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "部门更新")
public class VeDeptUpdDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @ApiModelProperty(value = "部门id", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门编号", dataType = "string")
    private String bh;

    @ApiModelProperty(value = "简称", dataType = "string")
    private String shortName;

    @ApiModelProperty(value = "上级部门ID", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "详细地址", dataType = "string")
    private String detailAddress;

    @ApiModelProperty(value = "状态（0停用 1启用）", dataType = "string")
    private String status;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}