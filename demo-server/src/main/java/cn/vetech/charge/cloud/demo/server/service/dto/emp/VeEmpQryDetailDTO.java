package cn.vetech.charge.cloud.demo.server.service.dto.emp;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "员工详情")
public class VeEmpQryDetailDTO {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}