package cn.vetech.charge.cloud.demo.fccapi.veemp.statusupd;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "员工信息禁用")
@XmlRootElement(name = "request")
public class StatusUpdEmpRequest extends FccApiRequest {

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @ApiModelProperty(value = "账号开通状态(0未开启，1已开启)", dataType = "string", notes = "账号开通状态(0未开启，1已开启)")
    private String accountStatus;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}