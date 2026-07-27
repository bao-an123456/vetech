package cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询员工详情信息")
@XmlRootElement(name = "request")
public class QryDetailEmpRequest extends FccApiRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工id", dataType = "string", notes = "员工id")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}