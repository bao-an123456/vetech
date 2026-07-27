package cn.vetech.charge.cloud.demo.fccapi.veposition.qrydetail;

import cn.vetech.charge.cloud.demo.fccapi.bean.QryDetailVePositionBean;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询任职详情")
@XmlRootElement(name = "response")
public class QryDetailPositionResponse extends FccApiResponse {

    @ApiModelProperty(value = "任职详情")
    private QryDetailVePositionBean positionDetail;

    public QryDetailVePositionBean getPositionDetail() {
        return positionDetail;
    }

    public void setPositionDetail(QryDetailVePositionBean positionDetail) {
        this.positionDetail = positionDetail;
    }
}