package cn.vetech.charge.cloud.demo.fccapi.empbydept;

import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiRequest;
import cn.vetech.charge.fccapi.FccApiUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询某部门下的所有员工")
@XmlRootElement(name = "request")
public class QryEmpByDeptRequest extends FccApiRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门id（部门表dept_id）", dataType = "string")
    private String deptId;

    @ApiModelProperty(value = "员工姓名", dataType = "string")
    private String name;

    @ApiModelProperty(value = "分页current", dataType = "int", notes = "当前页")
    private int current;

    @ApiModelProperty(value = "分页size", dataType = "int", notes = "分页size")
    private int size;

    @ApiModelProperty(value = "企业编号", dataType = "string", hidden = true)
    private String qybh;

    @ApiModelProperty(value = "登录用户信息", hidden = true)
    private FccApiUserVO login;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}