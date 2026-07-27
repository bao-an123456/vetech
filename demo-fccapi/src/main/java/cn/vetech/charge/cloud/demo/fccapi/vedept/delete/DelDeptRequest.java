package cn.vetech.charge.cloud.demo.fccapi.vedept.delete;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "删除部门信息")
@XmlRootElement(name = "request")
public class DelDeptRequest extends FccApiRequest {

    @ApiModelProperty(value = "部门id", dataType = "string", notes = "部门id")
    private String id;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}