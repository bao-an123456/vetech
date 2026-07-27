package cn.vetech.charge.cloud.demo.server.service.dto.emp;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工查询")
public class VeEmpQryDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "企业编号", dataType = "string", notes = "企业编号")
    private String qybh;

    @ApiModelProperty(value = "工号", dataType = "string", notes = "工号")
    private String gh;

    @ApiModelProperty(value = "员工姓名", dataType = "string", notes = "员工姓名")
    private String name;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string", notes = "账号开通状态(0未开启，1已开启)")
    private String accountStatus;

    //多表
    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String deptId;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}