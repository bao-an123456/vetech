package cn.vetech.charge.cloud.demo.server.service.dto.position;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "任职信息删除")
public class VePositionDelDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID主键", dataType = "string")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}