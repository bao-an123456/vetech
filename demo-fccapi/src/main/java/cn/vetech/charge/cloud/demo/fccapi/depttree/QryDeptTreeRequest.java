package cn.vetech.charge.cloud.demo.fccapi.depttree;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import cn.vetech.charge.fccapi.FccApiUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询部门子节点")
@XmlRootElement(name = "request")
public class QryDeptTreeRequest extends FccApiRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "上级部门id，不传则查询整棵树", dataType = "string")
    private String parentId;

    @ApiModelProperty(value = "企业编号", dataType = "string", hidden = true)
    private String qybh;

    @ApiModelProperty(value = "登录用户信息", hidden = true)
    private FccApiUserVO login;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}