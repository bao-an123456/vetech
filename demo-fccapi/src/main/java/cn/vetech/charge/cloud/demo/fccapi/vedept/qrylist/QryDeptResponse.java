package cn.vetech.charge.cloud.demo.fccapi.vedept.qrylist;

import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVeDeptBean;
import cn.vetech.charge.cloud.modules.utils.mapper.JsonMapper;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Api(value = "查询部门信息列表")
@XmlRootElement(name = "response")
public class QryDeptResponse extends FccApiResponse {

    @ApiModelProperty(value = "数据总条数", dataType = "int", notes = "数据总条数")
    private Integer total;

    @ApiModelProperty(value = "部门信息", dataType = "List", notes = "数据集合")
    private List<QryListVeDeptBean> veDeptList;

    @ApiModelProperty(value = "数据一页条数", dataType = "int", notes = "数据一页条数")
    private Integer size;

    @ApiModelProperty(value = "当前页", dataType = "int", notes = "当前页")
    private Integer current;

    @XmlElementWrapper(name = "veDeptList")
    @XmlElement(name = "veDept")
    public List<QryListVeDeptBean> getVeDeptList() {
        return veDeptList;
    }

    public void setVeDeptList(List<QryListVeDeptBean> veDeptList) {
        this.veDeptList = veDeptList;
    }

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}