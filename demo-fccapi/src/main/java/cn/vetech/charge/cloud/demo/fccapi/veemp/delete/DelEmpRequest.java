package cn.vetech.charge.cloud.demo.fccapi.veemp.delete;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "删除员工信息")
@XmlRootElement(name = "request")
public class DelEmpRequest extends FccApiRequest {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}