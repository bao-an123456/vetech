package cn.vetech.charge.cloud.demo.fccapi.empbydept;

import cn.vetech.charge.cloud.demo.fccapi.bean.EmpByDeptBean;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Api(value = "查询某部门下的所有员工")
@XmlRootElement(name = "response")
public class QryEmpByDeptResponse extends FccApiResponse {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工列表")
    private List<EmpByDeptBean> empList;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}