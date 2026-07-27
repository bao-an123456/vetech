package cn.vetech.charge.cloud.demo.fccapi.veemp.updpassword;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "修改员工密码")
@XmlRootElement(name = "request")
public class UpdEmpPwdRequest extends FccApiRequest {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @ApiModelProperty(value = "密码", dataType = "string", notes = "密码")
    private String password;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}