package cn.vetech.charge.cloud.demo.fccapi.veposition.qrylist;

import cn.vetech.charge.cloud.demo.fccapi.bean.QryListVePositionBean;
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
@Api(value = "查询任职信息列表")
@XmlRootElement(name = "response")
public class QryPositionResponse extends FccApiResponse {

    @ApiModelProperty(value = "数据总条数", dataType = "int")
    private Integer total;

    @ApiModelProperty(value = "任职信息", dataType = "List")
    private List<QryListVePositionBean> vePositionList;

    @ApiModelProperty(value = "数据一页条数", dataType = "int")
    private Integer size;

    @ApiModelProperty(value = "当前页", dataType = "int")
    private Integer current;

    @XmlElementWrapper(name = "vePositionList")
    @XmlElement(name = "vePosition")
    public List<QryListVePositionBean> getVePositionList() {
        return vePositionList;
    }

    public void setVePositionList(List<QryListVePositionBean> vePositionList) {
        this.vePositionList = vePositionList;
    }

    @Override
    public String toString() {
        return JsonMapper.nonEmptyMapper().toJson(this);
    }
}