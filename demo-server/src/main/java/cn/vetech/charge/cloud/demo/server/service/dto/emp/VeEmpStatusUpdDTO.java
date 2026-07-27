package cn.vetech.charge.cloud.demo.server.service.dto.emp;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工状态更新（禁用/启用）")
public class VeEmpStatusUpdDTO {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string", notes = "账号开通状态(0未开启，1已开启)")
    private String accountStatus;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}