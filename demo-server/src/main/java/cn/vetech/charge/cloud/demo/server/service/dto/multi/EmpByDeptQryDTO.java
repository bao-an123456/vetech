package cn.vetech.charge.cloud.demo.server.service.dto.multi;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "某部门下员工查询")
public class EmpByDeptQryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业编号", dataType = "string")
    private String qybh;

    @ApiModelProperty(value = "部门id（部门表dept_id）", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "员工姓名", dataType = "string")
    private String name;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}