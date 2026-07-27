package cn.vetech.charge.cloud.demo.fccapi.empwithdept_pos;

import cn.vetech.charge.cloud.demo.fccapi.bean.EmpWithDeptPosBean;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Api(value = "员工列表带部门/岗位信息查询")
@XmlRootElement(name = "response")
public class QryEmpWithDeptPosResponse extends FccApiResponse {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工列表")
    private List<EmpWithDeptPosBean> empList;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}