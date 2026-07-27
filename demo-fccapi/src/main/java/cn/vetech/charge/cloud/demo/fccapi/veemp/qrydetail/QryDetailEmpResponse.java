package cn.vetech.charge.cloud.demo.fccapi.veemp.qrydetail;

import cn.vetech.charge.cloud.demo.fccapi.bean.QryDetailVeEmpBean;
import cn.vetech.charge.fccapi.FccApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@Data
@Api(value = "查询员工详情")
@XmlRootElement(name = "response")
public class QryDetailEmpResponse extends FccApiResponse {

    @ApiModelProperty(value = "员工详情")
    private QryDetailVeEmpBean detail;

    public QryDetailVeEmpBean getDetail() {
        return detail;
    }

    public void setDetail(QryDetailVeEmpBean detail) {
        this.detail = detail;
    }
}