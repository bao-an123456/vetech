package cn.vetech.charge.cloud.demo.fccapi.veposition.delete;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "删除任职信息")
@XmlRootElement(name = "request")
public class DelPositionRequest extends FccApiRequest {

    @ApiModelProperty(value = "ID主键", dataType = "string", notes = "ID主键")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}