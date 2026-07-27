package cn.vetech.charge.cloud.demo.fccapi.veemp.qrylist;

import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVeEmpBean;
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
@Api(value = "查询员工信息列表")
@XmlRootElement(name = "response")
public class QryEmpResponse extends FccApiResponse {

    @ApiModelProperty(value = "数据总条数", dataType = "string", notes = "数据总条数")
    private Integer total;

    @ApiModelProperty(value = "员工信息", dataType = "List", notes = "数据集合")
    private List<QryListVeEmpBean> veEmpList;

    @ApiModelProperty(value = "数据一页条数", dataType = "string", notes = "数据一页条数")
    private Integer size;

    @ApiModelProperty(value = "当前页", dataType = "string", notes = "当前页")
    private Integer current;

    @XmlElementWrapper(name = "veEmpList")
    @XmlElement(name = "veEmpBean")
    public List<QryListVeEmpBean> getVeEmpList() {
        return veEmpList;
    }

    public void setVeEmpList(List<QryListVeEmpBean> veEmpList) {
        this.veEmpList = veEmpList;
    }

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}