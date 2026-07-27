package cn.vetech.charge.cloud.demo.server.service.dto.multi;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "部门树形结构查询")
public class DeptTreeQryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "上级部门id，不传则查整棵树", dataType = "string")
    private String parentId;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}