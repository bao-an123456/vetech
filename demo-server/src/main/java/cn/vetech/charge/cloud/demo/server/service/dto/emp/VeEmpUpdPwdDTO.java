package cn.vetech.charge.cloud.demo.server.service.dto.emp;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "修改密码")
public class VeEmpUpdPwdDTO {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @ApiModelProperty(value = "密码", dataType = "string", notes = "密码")
    private String password;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}