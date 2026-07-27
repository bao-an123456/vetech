package cn.vetech.charge.cloud.demo.fccapi.depttree;

import cn.vetech.charge.cloud.demo.fccapi.bean.DeptTreeBean;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Api(value = "查询部门子节点")
@XmlRootElement(name = "response")
public class QryDeptTreeResponse extends FccApiResponse {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部门子节点列表")
    private List<DeptTreeBean> deptList;

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}